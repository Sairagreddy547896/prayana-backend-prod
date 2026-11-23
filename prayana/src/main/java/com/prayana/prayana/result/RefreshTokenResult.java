package com.prayana.prayana.result;

public record RefreshTokenResult(
        String accessToken,
        String refreshToken
) {
}