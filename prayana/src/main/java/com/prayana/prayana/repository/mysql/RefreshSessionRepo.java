package com.prayana.prayana.repository.mysql;

import com.prayana.prayana.entity.RefreshSession;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RefreshSessionRepo extends JpaRepository<RefreshSession,Long> {


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT refreshSession
            FROM RefreshSession refreshSession
            JOIN FETCH refreshSession.myUser
            WHERE refreshSession.tokenHash = :tokenHash
            """)
    Optional<RefreshSession> findByTokenHashForUpdate(@Param("tokenHash") String tokenHash);
}