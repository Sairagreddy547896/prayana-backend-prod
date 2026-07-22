package com.prayana.prayana.securityconfig;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@AllArgsConstructor
@Configuration
public class DaoConfig {


    private CustomMyUserService customMyUserService;

    @Bean
    public AuthenticationProvider daoAuthenticationProvider(){

        DaoAuthenticationProvider authProvider=new DaoAuthenticationProvider(
                userDetailsService());
        authProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return authProvider;
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return customMyUserService;
    }

    @Bean
    public AuthenticationManager authenticationManager(){

        return new ProviderManager(daoAuthenticationProvider());
    }

    @Bean
    public PasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
