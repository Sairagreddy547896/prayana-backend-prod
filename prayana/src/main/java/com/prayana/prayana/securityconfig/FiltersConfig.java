package com.prayana.prayana.securityconfig;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@Configuration
@AllArgsConstructor
public class FiltersConfig{

    private final JWTAuthenticationFilter jwtAuthenticationFilter;



    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            DaoAuthenticationProvider daoAuthenticationProvider){

        return  http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(
                        auth-> {
                            auth.requestMatchers("/api/auth/**").permitAll();
                            auth.requestMatchers(HttpMethod.GET, "/api/places/**")
                                    .hasAnyAuthority("USER_ROLE","ADMIN_ROLE");
                            auth.requestMatchers(HttpMethod.POST,"/api/places/**")
                                    .hasAnyAuthority("ADMIN_ROLE");
                            auth.requestMatchers(HttpMethod.PUT,"/api/places/**")
                                    .hasAnyAuthority("ADMIN_ROLE");
                            auth.requestMatchers(HttpMethod.DELETE,"/api/places/**")
                                    .hasAnyAuthority("ADMIN_ROLE");
                            auth.anyRequest().authenticated();

                        }
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
