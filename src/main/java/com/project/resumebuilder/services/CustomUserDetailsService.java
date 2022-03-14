package com.project.resumebuilder.services;

import com.project.resumebuilder.models.CustomUserDetails;
import com.project.resumebuilder.models.User;
import com.project.resumebuilder.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository repository;


    //Overriding default load user functionality of spring security and loading our custom users for database
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUserName(username);
        return new CustomUserDetails(user);
    }
}