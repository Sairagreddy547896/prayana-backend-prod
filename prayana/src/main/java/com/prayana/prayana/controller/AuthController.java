package com.prayana.prayana.controller;

import com.prayana.prayana.customexceptions.RefreshTokenException;
import com.prayana.prayana.dto.MyUserRequest;
import com.prayana.prayana.dto.MyUserResponse;
import com.prayana.prayana.entity.MyUser;
import com.prayana.prayana.mapper.MyUserMapper;
import com.prayana.prayana.result.LogoutResult;
import com.prayana.prayana.result.RefreshTokenResult;
import com.prayana.prayana.securityconfig.JWTUtils;
import com.prayana.prayana.securityconfig.MyUserDetails;
import com.prayana.prayana.service.MyUserService;
import com.prayana.prayana.service.RefreshSessionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.HashSet;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final MyUserService myUserService;
    private final AuthenticationManager authenticationManager;
    private final JWTUtils jwtUtils;
    private final MyUserMapper myUserMapper;
    private final RefreshSessionService refreshSessionService;
    private static final Duration ACCESS_COOKIE_DURATION = Duration.ofMinutes(15);
    private static final Duration REFRESH_COOKIE_DURATION = Duration.ofDays(7);


    @PostMapping("/register")
    public ResponseEntity<MyUserResponse> registerUser(@RequestBody
                                                       MyUserRequest myUserRequest) {

        return new ResponseEntity<>(
                myUserService.registerMyUser(myUserRequest), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<MyUserResponse> loginUser(
            @RequestBody MyUserRequest myUserRequest,
            HttpServletResponse response) {

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                myUserRequest.getEmail(), myUserRequest.getPassword());
        Authentication auth = authenticationManager.authenticate(authentication);


        MyUserDetails myUserDetails =  (MyUserDetails) auth.getPrincipal();
        MyUser myUser=  myUserDetails.getMyUser();
        /*
         * Creating access token and updating in cookie
         */
        String accessToken = jwtUtils.generateToken(myUser);
        ResponseCookie accessCookie =
                createAccessTokenCookie(accessToken);
        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());

        /*
         * Creating refresh token and updating in cookie
         */
        String refreshToken =  refreshSessionService
                .createRefreshSession(myUser.getId());
        ResponseCookie refreshCookie =
                createRefreshTokenCookie(refreshToken);
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return new ResponseEntity<>(myUserMapper.entityToResponse(myUser), HttpStatus.OK);

    }

    @GetMapping("/me")
    public ResponseEntity<MyUserResponse> getCurrentUser(
            HttpServletResponse response){

        return null;

    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResult> logout(HttpServletResponse response){

        ResponseCookie cookie = ResponseCookie
                .from("access_token","")
                .httpOnly(false)
                .secure(false)
                .path("/")
                .sameSite("Lax")
                .maxAge(0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return new ResponseEntity<>(new LogoutResult("Logout successful",true),HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<Void> refresh(HttpServletResponse response,
                                        HttpServletRequest request
    ){
        String rawRefreshToken =   getCookieValue(
                request,"refresh_token");

        if(rawRefreshToken ==null || rawRefreshToken.isBlank()){
            throw new RefreshTokenException(
                    "REFRESH_TOKEN_MISSING",
                    "Refresh token cookie is missing"
            );
        }

        RefreshTokenResult refreshTokenResult = refreshSessionService
                .refreshTokens(rawRefreshToken);

        ResponseCookie accessTokenCookie =
                createAccessTokenCookie(refreshTokenResult.accessToken());

        ResponseCookie refreshTokenCookie =
                createRefreshTokenCookie(refreshTokenResult.refreshToken());

        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private String getCookieValue(HttpServletRequest request, String cookieName) {

        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookieName.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private ResponseCookie createAccessTokenCookie( String accessToken) {

        return ResponseCookie
                .from("access_token", accessToken)
                .httpOnly(true)
                .secure(false) // true when using production HTTPS
                .sameSite("Lax")
                .path("/")
                .maxAge(ACCESS_COOKIE_DURATION)
                .build();
    }

    private ResponseCookie createRefreshTokenCookie( String refreshToken) {

        return ResponseCookie
                .from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(false) // true when using production HTTPS
                .sameSite("Lax")
                .path("/")
                .maxAge(REFRESH_COOKIE_DURATION)
                .build();
    }
}