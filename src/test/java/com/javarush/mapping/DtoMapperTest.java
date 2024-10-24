package com.javarush.mapping;

import com.javarush.domain.Status;
import com.javarush.domain.Task;
import com.javarush.dto.TaskRequestTo;
import com.javarush.dto.TaskResponseTo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class DtoMapperTest {

    @Test
    void givenTaskRequestTo_whenConvert_thenReturnTask() {
        //given
        Integer expectedId = 8;
        Status expectedStatus = Status.DONE;
        String expectedDescription = "Test description";
        TaskRequestTo taskRequestTo = TaskRequestTo.builder()
                .id(expectedId)
                .status(expectedStatus)
                .description(expectedDescription)
                .build();
        //when then
        assertInstanceOf(Task.class, DtoMapper.MAPPER.from(taskRequestTo));
    }

    @Test
    void givenTaskRequestTo_whenConvert_thenReturnTaskWithEqualFields() {
        //given
        Integer expectedId = 8;
        Status expectedStatus = Status.DONE;
        String expectedDescription = "Test description";
        TaskRequestTo taskRequestTo = TaskRequestTo.builder()
                .id(expectedId)
                .status(expectedStatus)
                .description(expectedDescription)
                .build();
        //when
        Task task = DtoMapper.MAPPER.from(taskRequestTo);
        //then
        assertEquals(expectedId, task.getId());
        assertEquals(expectedStatus, task.getStatus());
        assertEquals(expectedDescription, task.getDescription());
    }

    @Test
    void givenTask_whenConvert_thenReturnTaskResponseTo() {
        //given
        Integer expectedId = 8;
        Status expectedStatus = Status.DONE;
        String expectedDescription = "Test description";
        Task task = Task.builder()
                .id(expectedId)
                .status(expectedStatus)
                .description(expectedDescription)
                .build();
        //when then
        assertInstanceOf(TaskResponseTo.class, DtoMapper.MAPPER.from(task));
    }

    @Test
    void givenTask_whenConvert_thenReturnTaskResponseToWithEqualFields() {
        //given
        Integer expectedId = 8;
        Status expectedStatus = Status.DONE;
        String expectedDescription = "Test description";
        Task task = Task.builder()
                .id(expectedId)
                .status(expectedStatus)
                .description(expectedDescription)
                .build();
        //when
        TaskResponseTo taskResponseTo = DtoMapper.MAPPER.from(task);
        //then
        assertEquals(expectedId, taskResponseTo.getId());
        assertEquals(expectedStatus, taskResponseTo.getStatus());
        assertEquals(expectedDescription, taskResponseTo.getDescription());
    }
}