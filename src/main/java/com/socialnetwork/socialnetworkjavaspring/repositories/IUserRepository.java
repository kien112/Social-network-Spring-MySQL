package com.socialnetwork.socialnetworkjavaspring.repositories;

import com.socialnetwork.socialnetworkjavaspring.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);

    Optional<User> findAllByEmail_AndPassword(String email, String password);

    @Query("select u from User u where " +
            "u.userId in (select r.userTarget.userId from Relation r " +
            "   where r.user.userId = :userId and r.type = 'FRIEND')")
    List<User> getUserFriends(String userId);
}
