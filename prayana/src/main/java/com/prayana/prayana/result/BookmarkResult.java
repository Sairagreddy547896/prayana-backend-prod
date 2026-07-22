package com.prayana.prayana.result;

import com.prayana.prayana.dto.BookmarkResponse;

public record BookmarkResult(
        boolean created,
        BookmarkResponse response
) {}
