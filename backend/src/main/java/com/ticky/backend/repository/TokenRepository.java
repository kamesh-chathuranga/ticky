package com.ticky.backend.repository;

import com.ticky.backend.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {
    @Query("""
            SELECT t
            FROM Token t inner join User u on t.user.id = u.id
            WHERE u.id = :userId
            AND (t.expired = false
            OR t.revoked = false)
            """)
    List<Token> findAllValidTokenByUser(Long userId);

    Optional<Token> findByToken(String token);
}
