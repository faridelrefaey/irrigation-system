package com.irrigationsystem.security.mapper

import com.irrigationsystem.security.dto.RegisterUserRequestDto
import com.irrigationsystem.security.entity.User
import com.irrigationsystem.security.enums.UserRoleEnum
import org.springframework.security.crypto.password.PasswordEncoder

object RegisterUserMapper {

    fun mapRequestToEntity(userRequestDto: RegisterUserRequestDto, passwordEncoder: PasswordEncoder, role: UserRoleEnum): User{
        return User(
            username = userRequestDto.username,
            password = passwordEncoder.encode(userRequestDto.password),
            role = role.name
        )
    }
}