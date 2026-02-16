package com.example.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // ===== SIGNUP =====
    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody User user) {
        if(userRepository.existsById(user.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        userRepository.save(user);
        return ResponseEntity.ok(user);
    }

    // ===== LOGIN =====
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody Map<String,String> body) {
        String email = body.get("email");
        String password = body.get("password");

        Optional<User> userOpt = userRepository.findByEmail(email);
        if(userOpt.isEmpty() || !userOpt.get().getPassword().equals(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(userOpt.get());
    }

    // ===== FETCH PROFILE =====
    @GetMapping("/profile/{userId}")
    public ResponseEntity<User> fetchProfile(@PathVariable String userId) {
        return userRepository.findById(userId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // ===== CHANGE USERNAME =====
    @PutMapping("/profile/username")
    public ResponseEntity<String> changeUsername(@RequestParam String userId, @RequestParam String newUsername) {
        Optional<User> userOpt = userRepository.findById(userId);
        if(userOpt.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

        User user = userOpt.get();
        user.setUsername(newUsername);
        userRepository.save(user);
        return ResponseEntity.ok("Username updated successfully");
    }

    // ===== CHANGE PASSWORD =====
    @PutMapping("/profile/password")
    public ResponseEntity<String> changePassword(@RequestParam String userId, @RequestParam String newPassword) {
        Optional<User> userOpt = userRepository.findById(userId);
        if(userOpt.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

        User user = userOpt.get();
        user.setPassword(newPassword);
        userRepository.save(user);
        return ResponseEntity.ok("Password updated successfully");
    }

    // ===== LIST USERS BASED ON ROLE =====
    @GetMapping("/users")
    public ResponseEntity<List<User>> listUsers(@RequestParam String requesterId) {
        Optional<User> requesterOpt = userRepository.findById(requesterId);
        if(requesterOpt.isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        User requester = requesterOpt.get();
        String role = requester.getRole();

        if(role.equals("admin") || role.equals("superhead")) {
            return ResponseEntity.ok(userRepository.findAll());
        } else if(role.equals("zoneLeader")) {
            return ResponseEntity.ok(userRepository.findByZone(requester.getZone()));
        } else {
            return ResponseEntity.ok(List.of(requester)); // members see only themselves
        }
    }
}

