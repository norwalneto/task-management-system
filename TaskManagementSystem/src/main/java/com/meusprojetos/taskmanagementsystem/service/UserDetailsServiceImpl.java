package com.meusprojetos.taskmanagementsystem.service;

import com.meusprojetos.taskmanagementsystem.model.User;
import com.meusprojetos.taskmanagementsystem.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final LoginAttemptService loginAttemptService;

    public UserDetailsServiceImpl(UserRepository userRepository, LoginAttemptService loginAttemptService){
        this.userRepository = userRepository;
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Usuario não encontrado"));

        if (loginAttemptService.isBlocked(username)) {
            throw new RuntimeException("Conta bloqueada devido a mútiplas tentativas de login fracassadas.");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                new HashSet<>()
        );
    }
}
