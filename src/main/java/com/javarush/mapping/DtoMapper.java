package com.javarush.mapping;

import com.javarush.domain.Task;
import com.javarush.dto.TaskRequestTo;
import com.javarush.dto.TaskResponseTo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DtoMapper {
    DtoMapper MAPPER = Mappers.getMapper(DtoMapper.class);

    Task from(TaskRequestTo taskRequestTo);
    TaskResponseTo from(Task task);
}
