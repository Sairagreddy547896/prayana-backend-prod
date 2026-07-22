package com.prayana.prayana.controller;

import com.prayana.prayana.dto.MyUserRequest;
import com.prayana.prayana.dto.MyUserResponse;
import com.prayana.prayana.entity.MyUser;
import com.prayana.prayana.mapper.MyUserMapper;
import com.prayana.prayana.result.LogoutResult;
import com.prayana.prayana.securityconfig.JWTUtils;
import com.prayana.prayana.securityconfig.MyUserDetails;
import com.prayana.prayana.service.MyUserService;
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

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final MyUserService myUserService;
    private final AuthenticationManager authenticationManager;
    private final JWTUtils jwtUtils;
    private final MyUserMapper myUserMapper;


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
        String jwtToken = jwtUtils.generateToken(myUser);

        ResponseCookie cookie = ResponseCookie
                .from("access_token",jwtToken)
                .httpOnly(false)
                .secure(false)
                .path("/")
                .sameSite("Lax")
                .maxAge(10 * 30)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

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
}
