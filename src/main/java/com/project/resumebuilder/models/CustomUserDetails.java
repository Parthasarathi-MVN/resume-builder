// This Custom User Details prevents Spring Security from creating a default user and display its password in  the console

package com.project.resumebuilder.models;

import com.project.resumebuilder.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.project.resumebuilder.models.User;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private String userName;
    private String password;
    private String userEmail;
//    private boolean active;
    private boolean enabled;

    @Autowired
    UserRepository userRepository;


    public CustomUserDetails(User user) {
        this.userName = user.getUserName();
        this.password = user.getPassword();
        this.userEmail = user.getEmail();
        this.enabled = user.isEnabled();
//        this.active = user.isActive();
    }





    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    } //Checking if the User is actually enabled after registering and clicking the link in the mail. In return statement the "enable" is a User attribute.
}
