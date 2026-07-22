package com.prayana.prayana.repository.mysql;

import com.prayana.prayana.entity.MyUser;
import com.prayana.prayana.entity.MyUserRoles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MyUserRoleRepo extends JpaRepository<MyUserRoles,Long> {

    Optional<MyUserRoles> findByRoleName(String roleName);
}
