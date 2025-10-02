package com.abid.ocr.db.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abid.ocr.db.models.User;
import com.abid.ocr.db.repository.UserRepository;

@Service
public class UserService {
  @Autowired
  private UserRepository userRepository;

  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  public Optional<User> getUserById(UUID id) {
    return userRepository.findById(id);
  }

  public Optional<User> getUserByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  public boolean userExistsByEmail(String email) {
    return getUserByEmail(email).isPresent();
  }

  public boolean isPasswordCorrect(String email, String hashedPassword) {
    Optional<User> userOptional = getUserByEmail(email);

    if (userOptional.isPresent()) {
      String storedHashedPassword = userOptional.get().getPasswordHash();
      return storedHashedPassword.equals(hashedPassword);
    }

    return false;
  }

  public User createUser(User user) {
    return userRepository.save(user);
  }

  public UUID getUserIdFromEmail(String email) {
    return userRepository.getUserIdByEmail(email);
  }
}
