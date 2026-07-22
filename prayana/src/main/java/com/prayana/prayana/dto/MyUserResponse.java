package com.prayana.prayana.dto;


import lombok.*;

import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class MyUserResponse {

    private Long id;
    private String name;
    private String email;
    private Set<String> roles;
}
