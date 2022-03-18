//Repositories are used for database read and write.

package com.project.resumebuilder.repositories;

import com.project.resumebuilder.models.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Integer> {
    UserProfile findByUserName(String userName);

}
