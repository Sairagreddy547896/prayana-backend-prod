package com.prayana.prayana.securityconfig;

import com.prayana.prayana.customexceptions.UserNotFoundException;
import com.prayana.prayana.entity.MyUser;
import com.prayana.prayana.repository.mysql.MyUserRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class CustomMyUserService implements UserDetailsService {


    private MyUserRepo myUserRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        MyUser myUser = myUserRepo.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException(
                        "user not found with given email "+email)
        );

        return new MyUserDetails(myUser);

    }
}
