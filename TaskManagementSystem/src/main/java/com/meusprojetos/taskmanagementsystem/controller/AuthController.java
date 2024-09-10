package com.meusprojetos.taskmanagementsystem.controller;

import com.meusprojetos.taskmanagementsystem.model.User;
import com.meusprojetos.taskmanagementsystem.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Registro de novo usuario
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());

        if (existingUser.isPresent()) {
            return new ResponseEntity<>("Username already taken", HttpStatus.BAD_REQUEST);
        }

        // Criptografar a senha antes de salvar
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(new HashSet<>());// Papeis podem ser configurados conforme necessidade
        user.getRoles().add("ROLE_USER");// Usuário padrão com role "ROLE_USER"
        userRepository.save(user);

        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }

    // Endpoint de login é tratado automaticamente pelo Spring Security (Basic Auth)
}
