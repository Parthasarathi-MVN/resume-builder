//Repositories are used for database read and write.

package com.project.resumebuilder.repositories;

import com.project.resumebuilder.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUserName(String userName);
    User findByEmail(String userEmail);

    @Query("SELECT u FROM User u WHERE u.verification_code = ?1")
    User findByverification_code(String code);
}
