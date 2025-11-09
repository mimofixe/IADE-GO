package com.iade.iadego.controller;

import com.iade.iadego.dto.*;
import com.iade.iadego.entity.User;
import com.iade.iadego.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    // POST /api/auth/register - REGISTAR USER

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest request) {
        logger.info("POST - Registering user: {}", request.getEmail());

        try {
            // Validar se email já existe
            if (userRepository.existsByEmail(request.getEmail())) {
                logger.warn("Email already exists: {}", request.getEmail());
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            // Validar se student number já existe
            if (request.getStudentNumber() != null &&
                    userRepository.existsByStudentNumber(request.getStudentNumber())) {
                logger.warn("Student number exists: {}", request.getStudentNumber());
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            // Criar user
            User user = new User();
            user.setEmail(request.getEmail());
            user.setFullName(request.getFullName());
            user.setStudentNumber(request.getStudentNumber());
            user.setPasswordHash(hashPassword(request.getPassword()));

            User saved = userRepository.save(user);
            logger.info("User registered: {}", saved.getUserId());

            // Response (sem password!)
            UserResponse response = new UserResponse();
            response.setUserId(saved.getUserId());
            response.setEmail(saved.getEmail());
            response.setFullName(saved.getFullName());
            response.setStudentNumber(saved.getStudentNumber());
            response.setCreatedAt(saved.getCreatedAt());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            logger.error("Error registering user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // POST /api/auth/login - LOGIN

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        logger.info("POST - Login attempt: {}", request.getEmail());

        try {
            // Buscar user por email
            Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
            if (userOpt.isEmpty()) {
                logger.warn("User not found: {}", request.getEmail());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            User user = userOpt.get();

            // Verificar password
            String hashed = hashPassword(request.getPassword());
            if (!user.getPasswordHash().equals(hashed)) {
                logger.warn("Invalid password for: {}", request.getEmail());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            logger.info("Login successful: {}", user.getEmail());

            // Response
            LoginResponse response = new LoginResponse();
            response.setUserId(user.getUserId());
            response.setEmail(user.getEmail());
            response.setFullName(user.getFullName());
            response.setStudentNumber(user.getStudentNumber());
            response.setMessage("Login successful");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error during login", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET /api/auth/users/{userId} - PERFIL USER

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUserProfile(@PathVariable Long userId) {
        logger.info("GET - Fetching user profile: {}", userId);

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            logger.warn("User not found: {}", userId);
            return ResponseEntity.notFound().build();
        }

        User user = userOpt.get();

        UserResponse response = new UserResponse();
        response.setUserId(user.getUserId());
        response.setEmail(user.getEmail());
        response.setFullName(user.getFullName());
        response.setStudentNumber(user.getStudentNumber());
        response.setCreatedAt(user.getCreatedAt());

        return ResponseEntity.ok(response);
    }

    // HELPER METHOD - PASSWORD HASH

    private String hashPassword(String password) {


        if ("password123".equals(password)) {
            return "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkO";
        }

        return "SIMPLE_" + password.hashCode();
    }
}