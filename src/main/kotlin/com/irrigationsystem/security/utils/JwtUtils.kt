package com.irrigationsystem.security.utils

import com.irrigationsystem.exceptions.AuthenticationException
import com.irrigationsystem.security.entity.User
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.nio.charset.StandardCharsets
import java.util.*

object JwtUtils {

    const val JWT_KEY: String = "bHLyBnBjOiaL+geXX1eIGsMWsknZ4sGHnZ0SYvCq7zSxOxk86xFxwq5Byd5306XYyN6VwXrtbtW"
    private val JWT_HEADER: String = "Authorization"
    const val expiryTime: Long = 1000 * 60 * 60
    val key = Keys.hmacShaKeyFor(JWT_KEY.toByteArray(StandardCharsets.UTF_8))

    fun generateToken(user: User): String{
        return Jwts.builder()
            .issuer("Farid Elrefaey")
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() + expiryTime))
            .claim("username", user.username)
            .claim("role", user.role)
            .signWith(key).compact()
    }

    fun validateToken(token: String){
        try{
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token)
        }
        catch (e: Exception){
            throw AuthenticationException("Invalid token", HttpStatus.UNAUTHORIZED)
        }
    }

    fun getRoleFromToken(): String {
        val token = getTokenFromContext()
            ?: throw AuthenticationException("Token not present", HttpStatus.UNAUTHORIZED)

        val key = Keys.hmacShaKeyFor(JWT_KEY.toByteArray(StandardCharsets.UTF_8))
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload["role"] as String
    }

    fun getUsernameFromToken(): String {
        val token = getTokenFromContext()
            ?: throw AuthenticationException("Token not present", HttpStatus.UNAUTHORIZED)

        val key = Keys.hmacShaKeyFor(JWT_KEY.toByteArray(StandardCharsets.UTF_8))
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload["username"] as String
    }

    private fun getTokenFromContext(): String?{
        val request: HttpServletRequest =
            (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request
        return request.getHeader(JWT_HEADER)?.substringAfter("Bearer ")
    }
}