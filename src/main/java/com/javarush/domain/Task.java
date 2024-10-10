package com.javarush.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "task", schema = "todo")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "varchar(100) NOT NULL")
    private String description;
    @Column(columnDefinition = "int(11) NOT NULL")
    @Enumerated(EnumType.ORDINAL)
    private Status status;
}
