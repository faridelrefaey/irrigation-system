package com.irrigationsystem.security.filter

import com.irrigationsystem.exceptions.AuthenticationException
import com.irrigationsystem.security.utils.JwtUtils
import io.jsonwebtoken.security.Keys
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtTokenValidator: OncePerRequestFilter() {
    private val JWT_HEADER: String = "Authorization"

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authentication = SecurityContextHolder.getContext().authentication
        val token = request.getHeader(JWT_HEADER)?.replace("Bearer ", "")
        if (authentication == null && !token.isNullOrBlank()) {
            JwtUtils.validateToken(token)
            val username = JwtUtils.getUsernameFromToken()
            val role = JwtUtils.getRoleFromToken()
            val authentication = UsernamePasswordAuthenticationToken(
                username, null,
                AuthorityUtils.commaSeparatedStringToAuthorityList(role)
            )
            SecurityContextHolder.getContext().authentication = authentication
        }
        filterChain.doFilter(request, response)
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return request.servletPath.equals("/auth/login") || request.servletPath.equals("/auth/register")
    }
}