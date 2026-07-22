package com.prayana.prayana.repository.mysql;

import com.prayana.prayana.entity.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MyUserRepo extends JpaRepository<MyUser,Long> {


    Optional<MyUser> findByEmail(String emailId);
}
