package com.meusprojetos.taskmanagementsystem.repository;

import com.meusprojetos.taskmanagementsystem.model.Task;
import com.meusprojetos.taskmanagementsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUser(User user);
    Optional<Task> findByIdAndUser(Long id, User user);
    List<Task> findByUserAndCompleted(User user, boolean completed);
}
