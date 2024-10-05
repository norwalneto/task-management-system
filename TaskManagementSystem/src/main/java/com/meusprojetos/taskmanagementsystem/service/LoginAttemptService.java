package com.meusprojetos.taskmanagementsystem.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginAttemptService {

    private final int MAX_ATTEMPT = 5;
    private Map<String, Integer> attemptsCache = new HashMap<>();

    public void loginSucceeded(String username) {
        attemptsCache.remove(username);
    }

    public void loginFailed(String username) {
        int attempts = attemptsCache.getOrDefault(username, 0);
        attempts++;
        attemptsCache.put(username, attempts);
    }

    public boolean isBlocked(String username){
        return attemptsCache.getOrDefault(username, 0) >= MAX_ATTEMPT;
    }
}
