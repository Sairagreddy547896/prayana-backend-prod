package com.prayana.prayana.mapper;

import com.prayana.prayana.dto.MyUserRequest;
import com.prayana.prayana.dto.MyUserResponse;
import com.prayana.prayana.entity.MyUser;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MyUserMapper {


    public MyUser requestToEntity(MyUserRequest myUserRequest){

        MyUser myUser=new MyUser();

        myUser.setName(myUserRequest.getName());
        myUser.setPassword(myUserRequest.getPassword());
        myUser.setEmail(myUserRequest.getEmail());
        return myUser;
    }

    public MyUserResponse entityToResponse(MyUser myUser){

        MyUserResponse myUserResponse=new MyUserResponse();

        myUserResponse.setEmail(myUser.getEmail());
        myUserResponse.setName(myUser.getName());
        myUserResponse.setId(myUser.getId());
        Set<String> roles= myUser.getRoles().stream().map(
                role -> role.getRoleName()
        ).collect(Collectors.toSet());
        myUserResponse.setRoles(roles);

        return myUserResponse;
    }
}
