package com.projects.blog.repositories;

import com.projects.blog.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query("""
        select t from Token t where t.token = :token and
        t.revoked_at IS NULL and t.expire_date >= NOW()
    """)
    Optional<Token> findByToken(String token);
}
