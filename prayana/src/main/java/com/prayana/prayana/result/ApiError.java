package com.prayana.prayana.result;

public record ApiError(
        int status,
        String code,
        String message
) {
}