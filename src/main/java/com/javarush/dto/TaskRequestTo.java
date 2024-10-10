package com.javarush.dto;

import com.javarush.domain.Status;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TaskRequestTo {
    String description;
    Status status;
}
