package com.irrigationsystem.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler
    fun handleIrrigationSystemException(ex: IrrigationSystemException): ResponseEntity<ErrorMessageModel>{
        val error = ErrorMessageModel(httpStatus = HttpStatus.BAD_REQUEST, status = HttpStatus.BAD_REQUEST.value(), message = ex.message)
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler
    fun handleAuthenticationException(ex: AuthenticationException): ResponseEntity<ErrorMessageModel>{
        val error = ErrorMessageModel(httpStatus = ex.statusCode, status = ex.statusCode.value(), message = ex.message)
        return ResponseEntity(error, ex.statusCode)
    }

}