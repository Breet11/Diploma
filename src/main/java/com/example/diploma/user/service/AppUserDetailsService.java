package com.example.diploma.user.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface AppUserDetailsService {
    UserDetails loadUserByUsername(String username);
}

