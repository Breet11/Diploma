package com.example.diploma.user.service;

import com.example.diploma.user.dto.CreateUserRequestDto;
import com.example.diploma.user.model.User;

public interface CreateUserService {
    User createUser(CreateUserRequestDto createUserRequestDto);
}


