package com.irrigationsystem.security.controller

import com.irrigationsystem.security.dto.LoginRequestDto
import com.irrigationsystem.security.dto.LoginResponseDto
import com.irrigationsystem.security.dto.RegisterUserRequestDto
import com.irrigationsystem.security.service.IAuthService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(@Autowired private val authService: IAuthService) {

    @PostMapping("/register")
    fun registerUser(@RequestBody userRequestDto: RegisterUserRequestDto): ResponseEntity<String> {
        return ResponseEntity.ok(authService.registerUser(userRequestDto))
    }

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequestDto): ResponseEntity<LoginResponseDto> {
        return ResponseEntity.ok(authService.login(loginRequest))
    }
}