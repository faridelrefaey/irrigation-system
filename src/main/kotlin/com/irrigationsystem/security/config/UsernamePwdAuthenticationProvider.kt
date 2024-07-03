package com.irrigationsystem.security.config

import com.irrigationsystem.exceptions.AuthenticationException
import com.irrigationsystem.security.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class UsernamePwdAuthenticationProvider
    (
    @Autowired private val userRepository: UserRepository,
    @Autowired private val passwordEncoder: PasswordEncoder): AuthenticationProvider {

    override fun authenticate(authentication: Authentication): Authentication {
        val username = authentication.name
        val plainPassword = authentication.credentials.toString()
        val user = userRepository.findByUsername(username)
            ?: throw AuthenticationException("User not found", HttpStatus.UNAUTHORIZED)

        if(!passwordEncoder.matches(plainPassword, user.password))
            throw AuthenticationException("Incorrect Password", HttpStatus.UNAUTHORIZED)
        val roles = listOf(SimpleGrantedAuthority(user.role))
        return UsernamePasswordAuthenticationToken(username, plainPassword, roles)
    }

    override fun supports(authentication: Class<*>?): Boolean {
        return UsernamePasswordAuthenticationToken::class.java.isAssignableFrom(authentication)
    }
}