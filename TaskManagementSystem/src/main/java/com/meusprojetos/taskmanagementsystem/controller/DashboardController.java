package com.meusprojetos.taskmanagementsystem.controller;

import com.meusprojetos.taskmanagementsystem.repository.TaskRepository;
import com.meusprojetos.taskmanagementsystem.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public DashboardController(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    // Endpoint para obter as metricas do sistema
    @GetMapping("/metrics")
    public ResponseEntity<Map<String, Object>> getMetrics() {
        Map<String, Object> metrics = new HashMap<>();

        //Quantidades total de usuarios
        long totalUsers = userRepository.count();
        metrics.put("totalUsers", totalUsers);

        //Quantidades total de tarefas
        long totalTask = taskRepository.count();
        metrics.put("totalTask", totalTask);

        //Quantidades de tarefas concluidas
        long completedTasks = taskRepository.countByCompleted(true);
        metrics.put("completedTasks", completedTasks);

        //Quantidades de tarefas pendentes
        long pendingTasks = taskRepository.countByCompleted(false);
        metrics.put("pendingTasks", pendingTasks);

        return new ResponseEntity<>(metrics, HttpStatus.OK);
    }
}
