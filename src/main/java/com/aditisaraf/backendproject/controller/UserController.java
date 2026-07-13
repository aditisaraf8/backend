package com.aditisaraf.backendproject.controller;

import com.aditisaraf.backendproject.dto.LoginRequest;
import com.aditisaraf.backendproject.dto.LoginResponse;
import com.aditisaraf.backendproject.entity.User;
import com.aditisaraf.backendproject.security.JwtService;
import com.aditisaraf.backendproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/signup")
    public User signup(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        Optional<User> user = userService.findByEmail(request.getEmail());

        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        if (!user.get().getPassword().equals(request.getPassword())) {
            return ResponseEntity.badRequest().body("Invalid password");
        }

        String token = jwtService.generateToken(user.get().getEmail());

        return ResponseEntity.ok(new LoginResponse(token));
    }
}