package com.quantumsoft.hrms.Human_Resource_Website.security;

import com.quantumsoft.hrms.Human_Resource_Website.entity.Admin;
import com.quantumsoft.hrms.Human_Resource_Website.entity.User;
import com.quantumsoft.hrms.Human_Resource_Website.repository.AdminRepository;
import com.quantumsoft.hrms.Human_Resource_Website.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Looking for user with username: " + username);

        // First check in User table
        Optional<User> optionalUser = userRepository.findByUsernameCaseSensitive(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getUsername())
                    .password(user.getPassword())
                    .authorities(new SimpleGrantedAuthority("ROLE_"+user.getRole().name()))
                    .accountExpired(false)
                    .accountLocked(false)
                    .build();
        }

        // Then check in Admin table
        Optional<Admin> optionalAdmin = adminRepository.findByUsernameIgnoreCase(username);
        if (optionalAdmin.isPresent()) {
            Admin admin = optionalAdmin.get();
            return org.springframework.security.core.userdetails.User
                    .withUsername(admin.getUsername())
                    .password(admin.getPassword())
                    .authorities(List.of(new SimpleGrantedAuthority("ROLE_ADMIN")))
                    .accountExpired(false)
                    .accountLocked(false)
                    .build();
        }

        throw new UsernameNotFoundException("User record not found in the database");
    }

}
