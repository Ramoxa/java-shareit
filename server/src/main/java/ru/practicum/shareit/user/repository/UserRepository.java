package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("""
            SELECT CASE WHEN COUNT(u)>0 THEN TRUE ELSE FALSE END
            FROM User AS u
            WHERE u.id!=:userId AND u.email=:email
            """)
    boolean hasEmail(long userId, String email);

    @Query("""
           SELECT CASE WHEN COUNT(u)> 0 THEN TRUE ELSE FALSE END
           FROM User AS u WHERE u.email=:email
           """)
    boolean hasEmail(String email);
}
