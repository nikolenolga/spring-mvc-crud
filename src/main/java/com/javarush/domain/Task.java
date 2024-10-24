package com.javarush.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "task", schema = "todo")
@ToString
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "varchar(100) NOT NULL")
    private String description;
    @Column(columnDefinition = "int(11) NOT NULL")
    @Enumerated(EnumType.ORDINAL)
    private Status status;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id)
                && Objects.equals(description, task.description)
                && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
