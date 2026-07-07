package com.salva.api.controller;

import com.salva.api.dto.LoginRequest;
import com.salva.api.dto.LoginResponse;
import com.salva.api.model.User;
import com.salva.api.service.FirebaseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
public class UserController {

    private final FirebaseService firebaseService;

    @Autowired
    public UserController(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@Valid @RequestBody User user) throws Exception {
        firebaseService.saveUser(user);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Usuário cadastrado com sucesso!");
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        Optional<User> user = firebaseService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
        
        if (user.isPresent()) {
            return ResponseEntity.ok(new LoginResponse("Login realizado com sucesso!", user.get()));
        }

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "E-mail ou senha inválidos");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
}