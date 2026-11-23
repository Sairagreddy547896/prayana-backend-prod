package com.prayana.prayana.securityconfig;

import com.prayana.prayana.entity.MyUser;
import lombok.AllArgsConstructor;
import lombok.ToString;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@AllArgsConstructor
@ToString
public class MyUserDetails implements UserDetails {


    private final MyUser myUser;

    public MyUser getMyUser(){
        return myUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return myUser.getRoles()
                .stream()
                .map(myUserRoles ->
                        new SimpleGrantedAuthority(myUserRoles.getRoleName()))
                .toList();
    }

    @Override
    public @Nullable String getPassword() {
        return myUser.getPassword();
    }

    @Override
    public String getUsername() {
        return myUser.getEmail();
    }

    public Long getUserId(){
        return myUser.getId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
