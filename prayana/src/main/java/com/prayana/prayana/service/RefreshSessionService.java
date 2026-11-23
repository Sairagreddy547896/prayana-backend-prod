package com.prayana.prayana.service;

import com.prayana.prayana.result.RefreshTokenResult;

public interface RefreshSessionService {

    String createRefreshSession(Long myUserId);

    RefreshTokenResult refreshTokens(String rawRefreshToken);
}