package com.prayana.prayana.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@AllArgsConstructor
@Setter
@Getter
@ToString
public class MyUserRequest {

    private String name;
    private String email;
    private String password;
    private Set<String> roles;
}
