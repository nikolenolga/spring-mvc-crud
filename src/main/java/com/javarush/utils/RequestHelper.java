package com.javarush.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RequestHelper {
    public final String TASKS = "tasks";
    public final String ERROR = "error";

    public String getRedirectAddress(int page, int limit) {
        return "redirect:/?%d=1&%d=10".formatted(page,limit);
    }
}
