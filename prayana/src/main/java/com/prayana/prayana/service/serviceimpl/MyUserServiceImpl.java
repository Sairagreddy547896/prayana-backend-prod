package com.prayana.prayana.service.serviceimpl;

import com.prayana.prayana.customexceptions.UserAlreadyExistWithEmailException;
import com.prayana.prayana.customexceptions.UserNotFoundException;
import com.prayana.prayana.customexceptions.UserRegistrationException;
import com.prayana.prayana.customexceptions.UserRoleNotFoundException;
import com.prayana.prayana.dto.MyUserRequest;
import com.prayana.prayana.dto.MyUserResponse;
import com.prayana.prayana.entity.MyUser;
import com.prayana.prayana.entity.MyUserRoles;
import com.prayana.prayana.mapper.MyUserMapper;
import com.prayana.prayana.repository.mysql.MyUserRepo;
import com.prayana.prayana.repository.mysql.MyUserRoleRepo;
import com.prayana.prayana.service.MyUserService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class MyUserServiceImpl implements MyUserService, UserDetailsService {

    private final MyUserRepo myUserRepo;
    private final MyUserMapper myUserMapper;
    private final PasswordEncoder passwordEncoder;
    private final MyUserRoleRepo myUserRoleRepo;

    @Override
    public MyUserResponse registerMyUser(MyUserRequest myUserRequest) {

        myUserRepo.findByEmail(myUserRequest.getEmail()).ifPresent(
                user -> {
                    throw new UserAlreadyExistWithEmailException(
                            "User already present with email"
                                    +myUserRequest.getEmail()
                    );
                }
        );

        MyUser  myUser  =  myUserMapper.requestToEntity(myUserRequest);
        myUser.setPassword(passwordEncoder.encode(myUser.getPassword()));

        Set<MyUserRoles> myUserRoles =
                myUserRequest.getRoles().stream().map(
                        userRoles -> myUserRoleRepo
                                .findByRoleName(userRoles)
                                .orElseThrow(() ->
                                        new UserRoleNotFoundException("Role is not present")
                                )).collect(Collectors.toSet());
        myUser.setRoles(myUserRoles);


        try {
            MyUser savedUser = myUserRepo.save(myUser);
            return  myUserMapper.entityToResponse(savedUser);
        }
        catch (DataAccessException ex){
            throw new UserRegistrationException(
                    "Failed to register user "+myUser.getEmail(),ex.getCause());
        }
    }

    @Override
    public MyUser loadUserByEmail(String emailId) {
        MyUser userFromDB   =     myUserRepo.findByEmail(emailId).orElseThrow(
                ()   -> new UserNotFoundException("user not found with emailId "+emailId)
        );
        return userFromDB;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
