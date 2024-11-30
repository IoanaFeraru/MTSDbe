package org.mastersdbis.mtsd.Repositories;

import org.mastersdbis.mtsd.Entities.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);

    User findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.username LIKE %:pattern%")
    List<User> searchByUsernamePattern(@Param("pattern") String pattern);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.password = :password WHERE u.id = :id")
    void updateUserPassword(@Param("id") Integer id, @Param("password") String password);
}
