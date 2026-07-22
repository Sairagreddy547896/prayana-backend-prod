package com.prayana.prayana.service;

import com.prayana.prayana.dto.MyUserRequest;
import com.prayana.prayana.dto.MyUserResponse;
import com.prayana.prayana.entity.MyUser;

public interface MyUserService {

    MyUserResponse registerMyUser(MyUserRequest myUserRequest);

    MyUser loadUserByEmail(String emailId);
}
