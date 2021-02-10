package com.rizvanchalilovas.accountingbe.security;

import com.rizvanchalilovas.accountingbe.models.User;
import com.rizvanchalilovas.accountingbe.repositories.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsServiceImlp")
public class UserDetailsServiceImlp implements UserDetailsService {

    private final UserJpaRepository userRepository;

    @Autowired
    public UserDetailsServiceImlp(UserJpaRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("User does not exist."));

        return SecurityUser.fromUser(user);
    }
}
