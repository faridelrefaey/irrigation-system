package com.irrigationsystem.security.service

import com.irrigationsystem.security.dto.LoginRequestDto
import com.irrigationsystem.security.dto.LoginResponseDto
import com.irrigationsystem.security.dto.RegisterUserRequestDto

interface IAuthService{

    fun registerUser(userRequest: RegisterUserRequestDto): String
    fun login(loginRequestDto: LoginRequestDto): LoginResponseDto
}