package com.gabriel.taskapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private List<Task> tasks = new ArrayList<>();
    private long nextId = 1;

    public static class Task {
        public long id;
        public String title;
        public String description;
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return tasks;
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        task.id = nextId++;
        tasks.add(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable long id) {
        return tasks.stream()
                .filter(t -> t.id == id)
                .findFirst()
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Task não encontrada"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable long id, @RequestBody Task updatedTask) {
        for (Task task : tasks) {
            if (task.id == id) {
                task.title = updatedTask.title;
                task.description = updatedTask.description;
                return ResponseEntity.ok(task);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task não encontrada");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable long id) {
        boolean removed = tasks.removeIf(t -> t.id == id);

        if (removed) {
            return ResponseEntity.ok("Task removida com sucesso");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Task não encontrada");
        }
    }
}
