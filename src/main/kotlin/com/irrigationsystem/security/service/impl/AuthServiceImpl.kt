package com.irrigationsystem.security.service.impl

import com.irrigationsystem.exceptions.AuthenticationException
import com.irrigationsystem.security.config.UsernamePwdAuthenticationProvider
import com.irrigationsystem.security.dto.LoginRequestDto
import com.irrigationsystem.security.dto.LoginResponseDto
import com.irrigationsystem.security.dto.RegisterUserRequestDto
import com.irrigationsystem.security.enums.UserRoleEnum
import com.irrigationsystem.security.mapper.RegisterUserMapper
import com.irrigationsystem.security.repository.UserRepository
import com.irrigationsystem.security.service.IAuthService
import com.irrigationsystem.security.utils.JwtUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


@Service
class AuthServiceImpl(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val passwordEncoder: PasswordEncoder,
    @Autowired private val usernamePwdAuthenticationProvider: UsernamePwdAuthenticationProvider
    ): IAuthService {

    override fun registerUser(userRequest: RegisterUserRequestDto): String {
        if(userRepository.findByUsername(userRequest.username) != null)
            throw AuthenticationException("Username already exists", HttpStatus.BAD_REQUEST)

        var role = UserRoleEnum.FARMER
        if(userRequest.username.contains("engineer", ignoreCase = true)) role = UserRoleEnum.ENGINEER

        userRepository.save(RegisterUserMapper.mapRequestToEntity(userRequest, passwordEncoder, role))
        return "User created successfully"
    }

    override fun login(loginRequestDto: LoginRequestDto): LoginResponseDto {
        try {
            usernamePwdAuthenticationProvider.authenticate(
                UsernamePasswordAuthenticationToken(loginRequestDto.username, loginRequestDto.password)
            )
        }
        catch (ex: AuthenticationException) {
            throw ex
        }
        return LoginResponseDto(token = JwtUtils.generateToken(userRepository.findByUsername(loginRequestDto.username)!!))
    }

}