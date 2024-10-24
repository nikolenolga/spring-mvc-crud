package com.javarush.controller;

import com.javarush.domain.Status;
import com.javarush.dto.TaskRequestTo;
import com.javarush.dto.TaskResponseTo;
import com.javarush.exception.AppException;
import com.javarush.service.TaskService;
import com.javarush.utils.Go;
import com.javarush.utils.Key;
import com.javarush.utils.View;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping({Go.INDEX, Go.EMPTY})
@SessionAttributes("statuses")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @ModelAttribute(Key.STATUSES)
    public Status[] statuses() {
        return Status.values();
    }

    @GetMapping
    public String readTasks(Model model,
                            @RequestParam(value = Key.PAGE, required = false, defaultValue = Key.DEFAULT_PAGE) int page,
                            @RequestParam(value = Key.LIMIT, required = false, defaultValue = Key.DEFAULT_LIMIT) int limit
    ) {
        int totalPages = taskService.countPages(limit);
        int currentPage = taskService.getCurrentPage(page, totalPages);
        List<TaskResponseTo> tasks = taskService.getCurrentPageList(currentPage, limit);

        model.addAttribute(Key.TASKS, tasks);
        model.addAttribute(Key.CURRENT_PAGE, currentPage);
        if (totalPages > 1) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute(Key.PAGE_NUMBERS, pageNumbers);
        }

        return View.TASKS;
    }

    @DeleteMapping("/{id}")
    public String deleteTask(@PathVariable(Key.ID) Integer id) {
        if (id <= 0) {
            throw new AppException("Invalid task id");
        }
        taskService.delete(id);

        return View.TASKS;
    }

    @PostMapping
    public String createTask(@RequestBody TaskRequestTo taskRequestTo) {

        taskService.create(taskRequestTo);
        return View.TASKS;
    }

    @PostMapping("/{id}")
    public String updateTask(
            @RequestBody TaskRequestTo taskRequestTo,
            @PathVariable(Key.ID) Integer id
    ) {
        taskService.edit(id, taskRequestTo.getDescription(), taskRequestTo.getStatus());

        return View.TASKS;
    }
}
