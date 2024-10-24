package com.javarush.controller;

import com.javarush.domain.Status;
import com.javarush.dto.TaskRequestTo;
import com.javarush.dto.TaskResponseTo;
import com.javarush.exception.AppException;
import com.javarush.service.TaskService;
import com.javarush.utils.RequestHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequestMapping({"/", ""})
@Controller
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }



    @GetMapping
    public String readTasks(Model model,
                        @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                        @RequestParam(value = "limit", required = false, defaultValue = "10") int limit
    ) {
        int totalPages = taskService.countPages(limit);
        if(page > totalPages){
            page = totalPages;
        }
        List<TaskResponseTo> tasks = taskService.getAll(limit * (page - 1), limit);

        model.addAttribute("statuses", Status.values());
        model.addAttribute("tasks", tasks);
        model.addAttribute("current_page", page);
        if(totalPages > 1) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1 , totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("page_numbers", pageNumbers);
        }

        System.out.println("simple mapping");
        System.out.println("-------------" + page + "--------------------------");
        System.out.println("-------------" + limit + "--------------------------");
        return RequestHelper.TASKS;
    }


    @DeleteMapping("/{id}")
    public String deleteTask(@PathVariable("id") Integer id) {
        if(id <= 0) {
            throw new AppException("Invalid task id");
        }

        //работает
        System.out.println("delete test contr");
        taskService.delete(id);
        System.out.println("-----------------------------------------------------------------------");

        return RequestHelper.TASKS;
    }

    @PostMapping
    public String createTask(@RequestBody TaskRequestTo taskRequestTo) {
        System.out.println("add task contr");
        System.out.println(taskRequestTo);

        taskService.create(taskRequestTo);
        return RequestHelper.TASKS;
    }

    @PostMapping("/{id}")
    public String updateTask(
                        @RequestBody TaskRequestTo taskRequestTo,
                        @PathVariable("id") Integer id
    ){
        System.out.println("save task contr");
        System.out.println("--------------------------------");
        taskService.edit(id, taskRequestTo.getDescription(), taskRequestTo.getStatus());
        //taskService.edit(id, description, status);
        return RequestHelper.TASKS;
    }
}
