package com.javarush.controller;

import com.javarush.domain.Status;
import com.javarush.domain.Task;
import com.javarush.dto.TaskResponceTo;
import com.javarush.exception.AppException;
import com.javarush.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Objects.isNull;

@RequestMapping({"/tt"})
@Controller
public class TasksController {
    private final TaskService taskService;

    public TasksController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/")
    public String tasks(Model model,
                            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                            @RequestParam(value = "limit", required = false, defaultValue = "10") int limit
    ) {
        List<Task> tasks = taskService.getAll(limit * (page - 1), limit);
        model.addAttribute("tasks", tasks);
        int totalPages = taskService.countPages(limit);
        List<Integer> pageNumbers = IntStream.range(1 , totalPages + 1).boxed().collect(Collectors.toList());
        model.addAttribute("page_numbers", pageNumbers);
        return "tasks";
    }

    @PostMapping("/{id}")
    public String editTask(Model model,
                       @PathVariable Integer id,
                       @RequestParam String description,
                       @RequestParam Status status
    ) {
        if(isNull(id) || id <= 0) {
            throw new AppException("Invalid task id");
        }
        taskService.edit(id, description, status);
        return tasks(model, 1, 10);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String addTask(Model model,
                           @RequestParam String description,
                           @RequestParam Status status
    ) {
        taskService.create(description, status);
        return tasks(model, 1, 10);
    }

//    @GetMapping("/{id}")
//    public String getTask(Model model, @PathVariable int id) {
//        Task task = taskService.getById(id);
//        model.addAttribute("task", task);
//        return tasks(model, 1, 10);
//    }

    @DeleteMapping("/{id}")
    public String deleteTask(Model model,
                             @PathVariable Integer id) {
        if(isNull(id) || id <= 0) {
            throw new AppException("Invalid task id");
        }
        taskService.delete(id);
        return tasks(model, 1, 10);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public String deleteTask(@PathVariable("id") Integer id,
                             @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                             @RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                             Model model) {
        if(isNull(id) || id <= 0) {
            throw new AppException("Invalid task id");
        }

        //работает
        System.out.println("delete test contr");
        System.out.println(id);

        taskService.delete(id);
        return tasks(model, page, limit);
    }
}
