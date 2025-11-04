package com.kami.tasklistserver.security;

import com.kami.tasklistserver.model.User;
import com.kami.tasklistserver.repository.UserRepository;
import com.kami.tasklistserver.service.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component("userDetailsService")
public class UserModelDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(UserModelDetailsService.class);
    private final UserRepository userRepository;

    public UserModelDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String normalized = username.trim().toLowerCase();
        User user = userRepository.findByUsername(normalized)
                .orElseThrow(() -> new UsernameNotFoundException("User '" + normalized + "' not found"));

        return UserDetailsImpl.build(user);
    }

    private org.springframework.security.core.userdetails.User mapToUserDetails(User user) {
        String role = "ROLE_" + user.getRole().trim().toUpperCase();
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}
