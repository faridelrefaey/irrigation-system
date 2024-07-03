package com.irrigationsystem.exceptions

import org.springframework.http.HttpStatus

class AuthenticationException(message: String, val statusCode: HttpStatus): RuntimeException(message) {
}