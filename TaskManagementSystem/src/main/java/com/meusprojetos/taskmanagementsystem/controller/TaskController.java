package com.meusprojetos.taskmanagementsystem.controller;

import com.meusprojetos.taskmanagementsystem.model.Task;
import com.meusprojetos.taskmanagementsystem.model.User;
import com.meusprojetos.taskmanagementsystem.repository.TaskRepository;
import com.meusprojetos.taskmanagementsystem.repository.UserRepository;
import com.meusprojetos.taskmanagementsystem.service.EmailService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public TaskController(TaskRepository taskRepository, UserRepository userRepository, EmailService emailService) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @GetMapping("/status")
    public ResponseEntity<List<Task>> getTaskByStatus(@RequestParam boolean completed) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            List<Task> tasks = taskRepository.findByUserAndCompleted(user.get(), completed);
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    // Criar nova tarefa
    @PostMapping
    public ResponseEntity<?> creatTask(@Valid @RequestBody Task task){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            task.setUser(user.get());
            Task savedTask = taskRepository.save(task);

            // Envia notificação por e-mail
            emailService.sendTaskNotification(user.get().getUsername(), "Nova Tarefa Criada",
                    "Sua nova tarefa '" + task.getDescription() + "' foi criada com sucesso!");

            return new ResponseEntity<>(savedTask, HttpStatus.CREATED);
        }else {
            return new ResponseEntity<>("Usuario não encontrado.", HttpStatus.UNAUTHORIZED);
        }
    }

    // Listar todas as tarefas do usuario autenticado
    @GetMapping
    public ResponseEntity<List<Task>> getAllTask() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            List<Task> tasks = taskRepository.findByUser(user.get());
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    //Buscar tarefa especifica do usuario autenticado
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            Optional<Task> task = taskRepository.findByIdAndUser(id, user.get());
            return task.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    //Atualizar tarefa do usuario autenticado
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task taskDetails) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            Optional<Task> task = taskRepository.findByIdAndUser(id, user.get());

            if (task.isPresent()) {
                Task existingTask = task.get();
                existingTask.setDescription(taskDetails.getDescription());
                existingTask.setCompleted(taskDetails.isCompleted());
                taskRepository.save(existingTask);
                return new ResponseEntity<>(existingTask, HttpStatus.OK);
            }else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    //Deletar tarefa do usuario autenticado
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            Optional<Task> task = taskRepository.findByIdAndUser(id, user.get());
            if (task.isPresent()) {
                taskRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    //Buscar Tarefa por Prioridade
    @GetMapping("/priority")
    public ResponseEntity<List<Task>> getTasksByPriority(@RequestParam Task.Priority priority) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            List<Task> tasks = taskRepository.findByUserAndPriority(user.get(), priority);
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
