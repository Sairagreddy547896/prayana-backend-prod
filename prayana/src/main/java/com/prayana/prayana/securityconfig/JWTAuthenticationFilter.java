package com.prayana.prayana.securityconfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prayana.prayana.result.ApiError;
import com.prayana.prayana.service.serviceimpl.BookmarkServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@AllArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JWTUtils jwtUtils;
    private final ObjectMapper objectMapper;

    private final Logger log=
            LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws
            ServletException, IOException {

        String bearerToken = request.getHeader("Authorization");
        String accessToken=getAccessToken(request,"access_token");
        if(bearerToken!=null) {
            accessToken = bearerToken.substring(7);
        }

        if(accessToken == null || accessToken.isBlank()){
            filterChain.doFilter(request,response);
            return;
        }
        try {
            if (jwtUtils.isTokenValid(accessToken)) {
                String myUserName = jwtUtils.extractUsername(accessToken);
                List<String> claimsFromJWT = (List<String>) jwtUtils
                        .getClaims(accessToken).get("UserRoles");

                List<SimpleGrantedAuthority> grantedAuthorities = claimsFromJWT.stream().map(
                        roles -> new SimpleGrantedAuthority(
                                roles
                        )
                ).toList();

                Authentication userAuthentication = SecurityContextHolder
                        .getContext()
                        .getAuthentication();

                if (myUserName != null && userAuthentication == null) {
                    SecurityContextHolder.getContext().setAuthentication(
                            new UsernamePasswordAuthenticationToken(
                                    myUserName,
                                    null,
                                    grantedAuthorities
                            )
                    );
                }
            }
        }
        catch (ExpiredJwtException e){
            SecurityContextHolder.clearContext();
            sendErrorResponse(response,
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "ACCESS_TOKEN_EXPIRED",
                    "access token expired");
            log.info("JWT token expired");
            return;
        }
        catch (JwtException | IllegalArgumentException e) {

            SecurityContextHolder.clearContext();

            sendErrorResponse(
                    response,
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "INVALID_ACCESS_TOKEN",
                    "Access token is invalid"
            );
            log.warn(
                    "Access token validation failed. IP: {}, reason: {}, exception: {}",
                    request.getRemoteAddr(),
                    e.getMessage(),
                    e.getClass().getSimpleName()
            );

            return;
        }

        filterChain.doFilter(request, response);
    }

    private String getAccessToken(HttpServletRequest request, String cookieName) {

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

    private void sendErrorResponse(HttpServletResponse response, int status,
                                   String code, String message)
            throws IOException {

        response.setStatus(status);
        response.setContentType("application/json");

        ApiError apiError = new ApiError(
                status,
                code,
                message
        );
        objectMapper.writeValue(
                response.getOutputStream(),
                apiError
        );
    }

    @Override
    public boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        String servletPath = request.getServletPath();
        return servletPath.equals("/api/auth/login")
                || servletPath.equals("/api/auth/register")
                || servletPath.equals("/api/auth/refresh")
                || servletPath.equals("/api/auth/logout");
    }
}