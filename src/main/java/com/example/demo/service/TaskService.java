package com.example.demo.service;

import com.example.demo.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.repository.TaskRepository;
import org.springframework.web.bind.annotation.GetMapping;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TaskService {
    @Autowired
    private  TaskRepository repository;


    public Task createTask(Task task) {

        task.setCreationDate(LocalDate.now());
        task.setStatus("A Fazer");
        Task savedTask = repository.save(task);
        System.out.println("Tarefa salva: " + savedTask);
        return savedTask;
    }

    @GetMapping
    public Map<String, List<Task>> getTasksOrganizedByColumn() {
        List<Task> tasks = repository.findAll();
        return tasks.stream()
                .collect(Collectors.groupingBy(Task::getStatus));
    }

    public Task moveTask(Long id) {
        Task task = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nova Tarefa" +id));

        switch (task.getStatus()) {
            case "A Fazer":
                task.setStatus("Em Progresso");
                break;
            case "Em Progresso":
                task.setStatus("Concluído");
                break;
            case "Concluído":
                //Essa lógica evita que uma tarefa que já está na coluna "Concluído" seja movida novamente.
                throw new IllegalStateException("'Concluído'");
            default:
                throw new IllegalArgumentException("status: Inválido  " + task.getStatus());
        }

        return repository.save(task);
    }

    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }

    public  Task updateTask(Long id, Task updatedTask) {
        Task task = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(""));
        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        task.setPriority(updatedTask.getPriority());
        task.setDueDate(updatedTask.getDueDate());
        return repository.save(task);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public  List<Task> getAllTasks() {
        return repository.findAll();
    }
}