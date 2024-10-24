package com.javarush.service;

import com.javarush.dao.TaskDAO;
import com.javarush.domain.Status;
import com.javarush.domain.Task;
import com.javarush.dto.TaskRequestTo;
import com.javarush.dto.TaskResponseTo;
import com.javarush.exception.AppException;
import com.javarush.mapping.DtoMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {
    private TaskService taskService;
    private TaskDAO taskDAOMock;
    private Stream<Task> tasks;
    private final int dbSize = 10;

    @BeforeEach
    void setUp() {
        taskDAOMock = mock(TaskDAO.class);
        taskService = new TaskService(taskDAOMock);
        tasks = IntStream
                .rangeClosed(1, dbSize)
                .mapToObj(n -> Task
                        .builder()
                        .id(n)
                        .status(Status.values()[n % Status.values().length])
                        .description("Test description " + n)
                        .build());
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAll() {
        //given
        int offset = 1;
        int limit = 5;
        when(taskDAOMock.getAll(offset, limit)).thenReturn(tasks.skip(offset).limit(limit));
        //when
        List<TaskResponseTo> taskResponses = taskService.getAll(offset, limit);
        //then
        assertEquals(limit, taskResponses.size());
    }

    @Test
    void getAllCount() {
        //given
        when(taskDAOMock.getAllCount()).thenReturn(dbSize);
        // when
        int actualSize = taskService.getAllCount();
        //then
        assertEquals(dbSize, actualSize);
    }

    @Test
    void countPages() {
        //given
        int limit = 0;
        //when then
        assertThrows(AppException.class, () -> taskService.countPages(limit));
    }

    @Test
    void countPages2() {
        //given
        int limit = dbSize;
        int expectedPages = 1;
        when(taskDAOMock.getAllCount()).thenReturn(dbSize);
        //when
        int actualPages = taskService.countPages(limit);
        //then
        assertEquals(expectedPages, actualPages);
    }

    @Test
    void countPages3() {
        //given
        int limit = 8;
        int expectedPages = 2;
        when(taskDAOMock.getAllCount()).thenReturn(dbSize);
        //when
        int actualPages = taskService.countPages(limit);
        //then
        assertEquals(expectedPages, actualPages);
    }

    @Test
    void getById() {
        //given
        Task task = tasks.toList().get(5);
        int id = task.getId();
        TaskResponseTo expected = DtoMapper.MAPPER.from(task);
        when(taskDAOMock.getById(id)).thenReturn(task);
        //when
        TaskResponseTo actual = taskService.getById(id);
        //then
        assertEquals(expected, actual);
    }

    @Test
    void edit() {
        //given//when
        int id = 18;
        when(taskDAOMock.getById(id)).thenReturn(null);
        //then
        assertThrows(AppException.class, () -> taskService.edit(id, "Any description", Status.DONE));
    }

    @Test
    void edit2() {
        //given
        Task task = tasks.toList().get(5);
        int id = task.getId();
        Status expectedStatus = Status.IN_PROGRESS;
        String expectedDescription = "New description";
        when(taskDAOMock.getById(id)).thenReturn(task);
        //when
        taskService.edit(id, expectedDescription, expectedStatus);
        //then
        verify(taskDAOMock, times(1)).getById(id);
        verify(taskDAOMock, times(1)).saveOrUpdate(any(Task.class));
        assertEquals(expectedStatus, task.getStatus());
        assertEquals(expectedDescription, task.getDescription());
    }

    @Test
    void create6() {
        //given
        TaskRequestTo taskRequestTo = TaskRequestTo
                .builder()
                .id(6)
                .build();
        // when // then
        assertThrows(AppException.class, () -> taskService.create(taskRequestTo));
    }

    @Test
    void create3() {
        //given
        TaskRequestTo taskRequestTo = null;
        //when //then
        assertThrows(AppException.class, () -> taskService.create(taskRequestTo));
    }

    @Test
    void create() {
        //given
        TaskRequestTo taskRequestTo = TaskRequestTo
                .builder()
                .id(0)
                .status(Status.DONE)
                .description("New description")
                .build();
        //when
        taskService.create(taskRequestTo);
        //then
        verify(taskDAOMock, times(1)).saveOrUpdate(any(Task.class));
    }

    @Test
    void delete() {
        //given //when
        int id = 18;
        when(taskDAOMock.getById(id)).thenReturn(null);
        //then
        assertThrows(AppException.class, () -> taskService.delete(id));
    }

    @Test
    void delete2() {
        //given
        Task task = tasks.toList().get(5);
        int id = task.getId();
        when(taskDAOMock.getById(id)).thenReturn(task);
        //when
        taskService.delete(id);
        //then
        verify(taskDAOMock, times(1)).delete(task);
    }
}