package com.example.demo.controller;

import com.example.demo.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.TaskService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskService service;


    @PostMapping
    public ResponseEntity<List<Task>> createTask(@RequestBody Task task) {
        Task createdTask = service.createTask(task);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public Map<String, List<Task>> getTasksOrganizedByColumn() {
        List<Task> tasks = service.getAllTasks();
        return tasks.stream()
                .collect(Collectors.groupingBy(Task::getStatus));
    }


    @PutMapping("/{id}/move")
    public Task moveTask(@PathVariable Long id) {
        return service.moveTask(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable() long id, @RequestBody Task updatedTask) {
        Task task = service.updateTask(id, updatedTask);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}