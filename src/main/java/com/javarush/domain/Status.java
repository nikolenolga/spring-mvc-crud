package com.javarush.domain;

import lombok.Getter;

@Getter
public enum Status {
    IN_PROGRESS("IN_PROGRESS"),
    DONE("DONE"),
    PAUSED("PAUSED");

    private final String name;

    Status(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
