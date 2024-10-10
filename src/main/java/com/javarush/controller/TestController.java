package com.javarush.controller;

import com.javarush.domain.Status;
import com.javarush.domain.Task;
import com.javarush.exception.AppException;
import com.javarush.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Objects.isNull;

@RequestMapping({"/", ""})
@Controller
public class TestController {

    private final TaskService taskService;

    public TestController(TaskService taskService) {
        this.taskService = taskService;
    }



    @GetMapping
    public String tasks(Model model,
                        @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                        @RequestParam(value = "limit", required = false, defaultValue = "10") int limit
    ) {
        model.addAttribute("statuses", Status.values());
        List<Task> tasks = taskService.getAll(limit * (page - 1), limit);
        model.addAttribute("tasks", tasks);
        int totalPages = taskService.countPages(limit);
        if(totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1 , totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("page_numbers", pageNumbers);
        }

        System.out.println("simple mapping");
        return "tasks";
    }

    @DeleteMapping("/{id}")
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
        taskService.delete(id);
        System.out.println("-----------------------------------------------------------------------");

        return tasks(model, page, limit);
    }

    @PostMapping
    public String addTask(Model model,
                          @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                          @RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                          @ModelAttribute Task task
    ) {
        System.out.println("add task contr");
        System.out.println(task);
        taskService.create(task.getDescription(), task.getStatus());
        return tasks(model, page, limit);
    }

    @PostMapping("/update")
    @ResponseBody
    public String saveTask(
                        @ModelAttribute Task task,
                        @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                        @RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                        Model model
    ){
        System.out.println("save task contr");
        System.out.println("--------------------------------");
        taskService.edit(task.getId(), task.getDescription(), task.getStatus());
        return tasks(model, page, limit);
    }
}
