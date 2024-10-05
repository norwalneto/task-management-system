package com.meusprojetos.taskmanagementsystem.controller;

import com.meusprojetos.taskmanagementsystem.model.Task;
import com.meusprojetos.taskmanagementsystem.model.User;
import com.meusprojetos.taskmanagementsystem.repository.TaskRepository;
import com.meusprojetos.taskmanagementsystem.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public ReportController(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/progress")
    public ResponseEntity<?> getProgressReport(@RequestParam boolean completed, @RequestParam String startDate, @RequestParam String endDate) {
        // Converte as strings de data para LocalDateTime
        LocalDateTime start = LocalDateTime.parse(startDate);
        LocalDateTime end = LocalDateTime.parse(endDate);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            List<Task> tasks = taskRepository.findByUserAndCompletedAndCreatedAtBetween(user.get(), completed, start, end);
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Usuario não autorizado", HttpStatus.UNAUTHORIZED);
        }
    }
}
