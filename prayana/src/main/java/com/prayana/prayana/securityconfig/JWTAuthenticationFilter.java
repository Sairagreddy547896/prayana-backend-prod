package com.prayana.prayana.securityconfig;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
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

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws
            ServletException, IOException {

        String bearerToken = request.getHeader("Authorization");


//        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
        String jwtToken="";
        if(request.getCookies()==null){
            filterChain.doFilter(request,response);
            return;
        }
        boolean access_sent=false;
        for(Cookie cookie:request.getCookies()){
            if(cookie.getName().equals("access_token")){
                if(cookie.getValue()==""){
                    filterChain.doFilter(request,response);
                    return;
                }
                else{
                    jwtToken=cookie.getValue();
                }
                access_sent=true;
            }
        }

        if(access_sent==false){
            filterChain.doFilter(request,response);
            return;
        }

        // String jwtToken = bearerToken.substring(7);

        if (jwtUtils.isTokenValid(jwtToken)) {
            String myUserName = jwtUtils.extractUsername(jwtToken);
            List<String> claimsFromJWT = (List<String>) jwtUtils
                    .getClaims(jwtToken).get("UserRoles");

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

        filterChain.doFilter(request, response);

    }
}


