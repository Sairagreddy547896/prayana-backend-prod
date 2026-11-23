package com.prayana.prayana.service.serviceimpl;

import com.prayana.prayana.customexceptions.RefreshTokenException;
import com.prayana.prayana.entity.MyUser;
import com.prayana.prayana.entity.RefreshSession;
import com.prayana.prayana.repository.mysql.MyUserRepo;
import com.prayana.prayana.repository.mysql.RefreshSessionRepo;
import com.prayana.prayana.result.RefreshTokenResult;
import com.prayana.prayana.securityconfig.JWTUtils;
import com.prayana.prayana.service.RefreshSessionService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.HexFormat;

@AllArgsConstructor
@Service
public class RefreshSessionServiceImpl implements RefreshSessionService {

    private final RefreshSessionRepo refreshSessionRepo;
    private final MyUserRepo myUserRepo;
    private final JWTUtils jwtUtils;

    @Override
    @Transactional
    public String createRefreshSession(Long myUserId) {

        String rawRefreshToken = generateRefreshToken();
        String tokenHash = hashToken(rawRefreshToken);

        MyUser myUser = myUserRepo.getReferenceById(myUserId);

        RefreshSession refreshSession = new RefreshSession();
        refreshSession.setMyUser(myUser);
        refreshSession.setCreatedAt(Instant.now());
        refreshSession.setExpiresAt(Instant.now().plus(7, ChronoUnit.DAYS));
        refreshSession.setTokenHash(tokenHash);

        refreshSessionRepo.save(refreshSession);
        return rawRefreshToken;
    }

    private String generateRefreshToken(){
        SecureRandom secureRandom = new SecureRandom();

        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);
    }


    @Transactional
    public RefreshTokenResult refreshTokens(String rawRefreshToken) {

        if (rawRefreshToken == null || rawRefreshToken.isBlank()) {
            throw new RefreshTokenException(
                    "REFRESH_TOKEN_MISSING",
                    "Refresh token is missing"
            );
        }

        String currentTokenHash = hashToken(rawRefreshToken);

        RefreshSession refreshSession =
                refreshSessionRepo
                        .findByTokenHashForUpdate(currentTokenHash)
                        .orElseThrow(() ->
                                new RefreshTokenException(
                                        "INVALID_REFRESH_TOKEN",
                                        "Refresh token is invalid"
                                )
                        );

        validateRefreshSession(refreshSession);

        MyUser user = refreshSession.getMyUser();

        /*
         * Generating access token using trusted database user
         */
        String newAccessToken = jwtUtils.generateToken(user);

        String newRawRefreshToken = generateRefreshToken();
        String newTokenHash = hashToken(newRawRefreshToken);

        refreshSession.setTokenHash(newTokenHash);

        refreshSessionRepo.save(refreshSession);

        return new RefreshTokenResult(
                newAccessToken,
                newRawRefreshToken
        );
    }

    @Transactional
    public void revoke(String rawRefreshToken) {

        if (rawRefreshToken == null || rawRefreshToken.isBlank()) {
            return;
        }

        String tokenHash = hashToken(rawRefreshToken);

        refreshSessionRepo
                .findByTokenHashForUpdate(tokenHash)
                .ifPresent(refreshSession -> {
                    refreshSession.setRevoked(true);
                    refreshSessionRepo.save(refreshSession);
                });
    }

    private void validateRefreshSession( RefreshSession refreshSession) {

        if (refreshSession.isRevoked()) {
            throw new RefreshTokenException(
                    "REFRESH_TOKEN_REVOKED",
                    "Refresh session has been revoked"
            );
        }

        if (!refreshSession.getExpiresAt().isAfter(Instant.now())) {
            /*
             * Setting refresh token as expired after 7days.
             */
            refreshSession.setRevoked(true);

            throw new RefreshTokenException(
                    "REFRESH_TOKEN_EXPIRED",
                    "Refresh session has expired"
            );
        }
    }

    private String hashToken(String rawToken) {

        try {
            MessageDigest messageDigest =
                    MessageDigest.getInstance("SHA-256");

            byte[] hash = messageDigest.digest(
                    rawToken.getBytes(StandardCharsets.UTF_8)
            );

            return HexFormat.of().formatHex(hash);

        } catch (NoSuchAlgorithmException exception) {
            /*
             * SHA-256 is required by the Java platform, so this represents
             * a server/runtime configuration problem rather than bad input.
             */
            throw new IllegalStateException(
                    "SHA-256 algorithm is not available",
                    exception
            );
        }
    }

}