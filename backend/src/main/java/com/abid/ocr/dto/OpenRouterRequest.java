package com.abid.ocr.dto;

import java.util.List;
import java.util.Map;

public class OpenRouterRequest {
    private String model;
    private List<Message> messages;

    public static class Message {
        private String role;
        private List<Content> content;

        public static class Content {
            private String type;
            private String text; // only used for text type
            private Map<String, String> image_url; // for image_url type

            // Getters & Setters
            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public Map<String, String> getImage_url() {
                return image_url;
            }

            public void setImage_url(Map<String, String> image_url) {
                this.image_url = image_url;
            }
        }

        // Getters & Setters
        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public List<Content> getContent() {
            return content;
        }

        public void setContent(List<Content> content) {
            this.content = content;
        }
    }

    // Getters & Setters
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

}
