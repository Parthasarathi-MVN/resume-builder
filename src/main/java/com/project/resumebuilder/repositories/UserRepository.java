package com.project.resumebuilder.repositories;

import com.project.resumebuilder.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUserName(String userName);
}
