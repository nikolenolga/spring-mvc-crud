package com.javarush.dto;

import com.javarush.domain.Status;
import lombok.*;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class TaskRequestTo {
    Integer id;
    String description;
    Status status;
}
