package com.javarush.service;

import com.javarush.dao.TaskDAO;
import com.javarush.domain.Status;
import com.javarush.domain.Task;
import com.javarush.exception.AppException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Objects.isNull;

@Service
public class TaskService {
    private final TaskDAO taskDAO;

    public TaskService(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    public List<Task> getAll(int offset, int limit) {
        return taskDAO.getAll(offset,limit);
    }

    public int countPages(int limit) {
        if (limit <= 0) {
            throw new AppException("Incorrect pages limit - %d".formatted(limit));
        }
        return (int) Math.ceil(1.0 * getAllCount() / limit);
    }

    public int getAllCount() {
        return taskDAO.getAllCount();
    }

    public Task getById(int id) {
        return taskDAO.getById(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void edit(int id, String description, Status status) {
        Task task = taskDAO.getById(id);
        if(isNull(task)) {
            throw new AppException("Task with id = %d not found".formatted(id));
        }
        task.setDescription(description);
        task.setStatus(status);
        taskDAO.saveOrUpdate(task);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void create(String description, Status status) {
        Task task = Task.builder()
                .description(description)
                .status(status)
                .build();
        taskDAO.saveOrUpdate(task);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(int id) {
        Task task = taskDAO.getById(id);
        if(isNull(task)) {
            throw new AppException("Task with id = %d not found".formatted(id));
        }
        taskDAO.delete(task);
    }

}
