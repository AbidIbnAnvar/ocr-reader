package com.abid.ocr.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abid.ocr.db.models.User;
import com.abid.ocr.db.services.UserService;
import com.abid.ocr.dto.ErrorResponse;
import com.abid.ocr.dto.auth.LoginRequest;
import com.abid.ocr.dto.auth.LoginResponse;
import com.abid.ocr.dto.auth.SignupRequest;
import com.abid.ocr.dto.auth.SignupResponse;
import com.abid.ocr.exception.ReceiptProcessingException;
import com.abid.ocr.service.JwtService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(
            UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest request) {
        try {
            String email = request.getEmail();
            if (userService.userExistsByEmail(email)) {
                String hashedPassword = request.getPassword();
                if (!userService.isPasswordCorrect(email, hashedPassword)) {
                    throw new RuntimeException("Incorrect password");
                }
                String jwt;
                try {
                    jwt = JwtService.generateToken(email);
                } catch (Exception tokenEx) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ErrorResponse("Token generation failed: " + tokenEx.getMessage()));
                }

                LoginResponse response = new LoginResponse(jwt);
                return ResponseEntity.ok(response);
            } else {
                throw new RuntimeException("User not found");
            }
        } catch (ReceiptProcessingException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error authenticating user:" + e.getMessage()));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest request) {
        try {
            String username = request.getUsername();
            String email = request.getEmail();
            String hashedPassword = request.getPassword();
            userService.createUser(new User(username, email, hashedPassword));
            String jwt;
            try {
                jwt = JwtService.generateToken(email);
            } catch (Exception tokenEx) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorResponse("Token generation failed: " + tokenEx.getMessage()));
            }
            SignupResponse response = new SignupResponse(jwt);
            return ResponseEntity.ok(response);
        } catch (ReceiptProcessingException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error registering user:" + e.getMessage()));
        }
    }

}
