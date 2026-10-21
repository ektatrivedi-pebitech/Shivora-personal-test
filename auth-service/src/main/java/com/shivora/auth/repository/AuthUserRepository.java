
package com.shivora.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shivora.auth.entity.AuthUser;

public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {

    @Query(value = """
            SELECT *
            FROM dbo.auth_users
            WHERE email = :email
              AND is_deleted = 0
            """, nativeQuery = true)
    Optional<AuthUser> findActiveByEmail(
            @Param("email") String email
    );
    
    boolean existsByEmailIgnoreCaseAndIsDeletedFalse(String email);

    boolean existsByMobileAndIsDeletedFalse(String mobile);
}
