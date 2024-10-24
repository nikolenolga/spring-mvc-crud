package com.javarush.service;

import com.javarush.dao.TaskDAO;
import com.javarush.domain.Status;
import com.javarush.domain.Task;
import com.javarush.dto.TaskRequestTo;
import com.javarush.dto.TaskResponseTo;
import com.javarush.exception.AppException;
import com.javarush.mapping.DtoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Objects.isNull;

@Service
public class TaskService {
    private final TaskDAO taskDAO;

    public TaskService(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    public List<TaskResponseTo> getAll(int offset, int limit) {
        return taskDAO
                .getAll(offset,limit)
                .map(DtoMapper.MAPPER::from)
                .toList();
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

    public TaskResponseTo getById(int id) {
        Task task = taskDAO.getById(id);
        return DtoMapper.MAPPER.from(task);
    }

    @Transactional
    public void edit(int id, String description, Status status) {
        Task task = taskDAO.getById(id);
        if(isNull(task)) {
            throw new AppException("Task with id = %d not found".formatted(id));
        }
        task.setDescription(description);
        task.setStatus(status);
        taskDAO.saveOrUpdate(task);
    }

    @Transactional
    public void create(TaskRequestTo taskRequestTo) {
        if(isNull(taskRequestTo) || (taskRequestTo.getId() != null && taskRequestTo.getId() != 0)) {
            throw new AppException("Can't create task %s".formatted(taskRequestTo));
        }
        taskDAO.saveOrUpdate(DtoMapper.MAPPER.from(taskRequestTo));
    }

    @Transactional
    public void delete(int id) {
        Task task = taskDAO.getById(id);
        if(isNull(task)) {
            throw new AppException("Task with id = %d not found".formatted(id));
        }
        taskDAO.delete(task);
    }

}
