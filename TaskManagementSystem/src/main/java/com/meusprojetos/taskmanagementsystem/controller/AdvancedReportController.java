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
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reports/advanced")
public class AdvancedReportController {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public AdvancedReportController(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    //Relatorio de tempo medio de conclusão
    @GetMapping("/average-time")
    public ResponseEntity<?> getAverageCompletionTime(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            List<Task> completedTasks = taskRepository.findByUserAndCompleted(user.get(), true);

            if (completedTasks.isEmpty()) {
                return new ResponseEntity<>("Nenhuma tarefa concluida", HttpStatus.OK);
            }

            Duration totalDuration = completedTasks.stream()
                    .filter(task -> task.getCompletedAt() != null)
                    .map(task -> Duration.between(task.getCreatedAt(), task.getCompletedAt()))
                    .reduce(Duration::plus)
                    .orElse(Duration.ZERO);

            long averageSeconds = totalDuration.getSeconds() / completedTasks.size();
            Duration averageDuration = Duration.ofSeconds(averageSeconds);

            return new ResponseEntity<>("Tempo médio de conclusão: " + averageDuration.toHours() + " horas", HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Usuario não autorizado", HttpStatus.UNAUTHORIZED);
        }
    }
}
