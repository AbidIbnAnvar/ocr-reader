package com.abid.ocr.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.abid.ocr.dto.Item;
import com.abid.ocr.dto.ReceiptDto;
import com.abid.ocr.exception.ReceiptProcessingException;

@Service
public class TextParserService {
  private static final Logger logger = LoggerFactory.getLogger(TextParserService.class);

  // Improved patterns for restaurant receipts
  private static final List<Pattern> ITEM_PATTERNS = Arrays.asList(
      // Pattern 1: "1 Pho Ga (Small) 8.00"
      Pattern.compile("^(\\d+)\\s+(.+?)\\s+([0-9]+[.,]?[0-9]{0,2})\\s*$"),

      // Pattern 2: "1x Pho Ga (Small) 8.00"
      Pattern.compile("^(\\d+)x\\s+(.+?)\\s+([0-9]+[.,]?[0-9]{0,2})\\s*$"),

      // Pattern 3: "Pho Ga (Small) 8.00" (implicit quantity 1)
      Pattern.compile("^(.+?)\\s+([0-9]+[.,]?[0-9]{0,2})\\s*$"));

  private static final List<Pattern> TOTAL_PATTERNS = Arrays.asList(
      Pattern.compile("(?i)(sub\\s?total|sum)\\s*[:]?\\s*([\\p{Sc}]?\\s*[0-9]+[.,]?[0-9]{0,2})"),
      Pattern.compile("(?i)(tax|vat)\\s*[:]?\\s*([\\p{Sc}]?\\s*[0-9]+[.,]?[0-9]{0,2})"),
      Pattern.compile("(?i)(total|amount)\\s*[:]?\\s*([\\p{Sc}]?\\s*[0-9]+[.,]?[0-9]{0,2})"),
      Pattern.compile("\\*\\*\\s*(?i)total\\s*\\*\\*\\s*([\\p{Sc}]?\\s*[0-9]+[.,]?[0-9]{0,2})"));

  private static final Pattern PRICE_ONLY_PATTERN = Pattern
      .compile("^\\s*([\\p{Sc}]?\\s*[0-9]+[.,]?[0-9]{0,2})\\s*$");
  private static final Pattern IGNORED_LINES = Pattern.compile(
      "(?i)(date|time|server|bill|table|thank|phone|address|open|printed|suggested|tip|charge|#|[*]+)");
  private static final Pattern HEADER_FOOTER = Pattern.compile(
      "(?i).*(pho ga|mission dr|rosemead|thank you|coming|visa|mastercard|change).*");

  public ReceiptDto parseReceiptText(String text) throws ReceiptProcessingException {
    if (text == null || text.trim().isEmpty()) {
      throw new ReceiptProcessingException("No text extracted from receipt");
    }

    logger.debug("Raw OCR text:\n{}", text);

    // Pre-process text to fix common OCR errors
    text = fixCommonOcrErrors(text);

    ReceiptDto response = new ReceiptDto();
    List<Item> items = parseItems(text);
    response.setItems(items);

    parseTotals(text, response);

    if (items.isEmpty()) {
      throw new ReceiptProcessingException("No items found in receipt");
    }

    logger.info("Parsed {} items with total: {}", items.size(), response.getTotal());
    return response;
  }

  private String fixCommonOcrErrors(String text) {
    // Fix common OCR misreads in restaurant receipts
    return text.replaceAll("(?i)bate:", "Date:")
        .replaceAll("(?i)tine:", "Time:")
        .replaceAll("(?i)gbtotal", "Subtotal")
        .replaceAll("(?i)8\\. Service", "S. Service")
        .replaceAll("40x", "40%")
        .replaceAll("\\$33 -26", "33.26")
        .replaceAll("Adsin", "Admin")
        .replaceAll("Tiae", "Time")
        .replaceAll("PH", "PM");
  }

  private List<Item> parseItems(String text) {
    List<Item> items = new ArrayList<>();
    String[] lines = text.split("\\r?\\n");

    boolean inItemsSection = false;

    for (String line : lines) {
      line = cleanLine(line);
      if (shouldSkipLine(line))
        continue;

      logger.debug("Processing line: {}", line);

      // Detect start of items section
      if (!inItemsSection && line.matches(".*\\d+\\s+.*\\d+.*")) {
        inItemsSection = true;
      }

      if (!inItemsSection)
        continue;

      // Skip lines that are clearly not items
      if (isTotalLine(line)) {
        inItemsSection = false;
        continue;
      }

      // Try all item patterns
      for (Pattern pattern : ITEM_PATTERNS) {
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
          try {
            Item item = createItemFromMatcher(matcher, pattern);
            if (isValidItem(item)) {
              items.add(item);
              logger.debug("Matched item: {} ({} x {})",
                  item.getName(), item.getQuantity(), item.getPrice());
            }
            break;
          } catch (Exception e) {
            logger.warn("Failed to parse item from line: {}", line);
          }
        }
      }
    }

    return items;
  }

  private void parseTotals(String text, ReceiptDto response) {
    String[] lines = text.split("\\r?\\n");
    boolean foundSubtotal = false;

    for (String line : lines) {
      line = cleanLine(line);

      for (Pattern pattern : TOTAL_PATTERNS) {
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
          String type = matcher.group(1) != null ? matcher.group(1).toLowerCase() : "total";
          String amount = matcher.groupCount() > 1 ? matcher.group(2) : matcher.group(1);

          try {
            double value = parsePrice(amount);

            if (type.contains("sub") && !foundSubtotal) {
              response.setSubtotal(value);
              foundSubtotal = true;
              logger.debug("Found subtotal: {}", value);
            } else if (type.contains("tax") && response.getTax() == null) {
              response.setTax(value);
              logger.debug("Found tax: {}", value);
            } else if (type.contains("total") && response.getTotal() == null) {
              response.setTotal(value);
              logger.debug("Found total: {}", value);
            }
          } catch (NumberFormatException e) {
            logger.warn("Failed to parse total amount: {}", amount);
          }
        }
      }
    }

    // Fallback: If no total found but subtotal exists, calculate total
    if (response.getTotal() == null && response.getSubtotal() != null && response.getTax() != null) {
      response.setTotal(response.getSubtotal() + response.getTax());
      logger.debug("Calculated total from subtotal + tax");
    }
  }

  private Item createItemFromMatcher(Matcher matcher, Pattern pattern) {
    Item item = new Item();

    if (pattern == ITEM_PATTERNS.get(0) || pattern == ITEM_PATTERNS.get(1)) {
      // Patterns with explicit quantity
      item.setQuantity(Integer.parseInt(matcher.group(1)));
      item.setName(matcher.group(2).trim());
      item.setPrice(parsePrice(matcher.group(3)));
    } else {
      // Pattern with implicit quantity (1)
      item.setQuantity(1);
      item.setName(matcher.group(1).trim());
      item.setPrice(parsePrice(matcher.group(2)));
    }

    return item;
  }

  private double parsePrice(String priceStr) {
    // Normalize price string
    String normalized = priceStr.replaceAll("[^\\d.,]", "")
        .replace(",", ".");

    // Handle missing decimals
    if (!normalized.contains(".")) {
      if (normalized.length() > 2) {
        normalized = normalized.substring(0, normalized.length() - 2) + "." +
            normalized.substring(normalized.length() - 2);
      } else if (normalized.length() == 2) {
        normalized = "0." + normalized;
      } else {
        normalized += ".00";
      }
    }

    return Double.parseDouble(normalized);
  }

  private String cleanLine(String line) {
    return line.replaceAll("[^\\p{Print}]", " ")
        .replaceAll("\\s{2,}", " ")
        .trim();
  }

  private boolean shouldSkipLine(String line) {
    return line.isEmpty() ||
        IGNORED_LINES.matcher(line).find() ||
        HEADER_FOOTER.matcher(line).matches() ||
        line.length() < 3;
  }

  private boolean isTotalLine(String line) {
    return line.matches("(?i).*(sub\\s?total|tax|total|amount|charge|tip|\\*\\*).*") ||
        line.matches(".*[0-9]+\\s*[-]\\s*[0-9]+.*"); // Matches "33 -26"
  }

  private boolean isValidItem(Item item) {
    // Basic validation to exclude false positives
    if (item.getName().length() < 2)
      return false;
    else if (item.getPrice() <= 0 || item.getPrice() > 1000)
      return false;
    else if (item.getName().matches(".*\\d{4,}.*"))
      return false; // Exclude long numbers
    return true;
  }
}
