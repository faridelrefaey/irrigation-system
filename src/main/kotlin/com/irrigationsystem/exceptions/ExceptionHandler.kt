package com.irrigationsystem.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler
    fun handleIdDoesNotExistException(ex: IdDoesNotExistException): ResponseEntity<ErrorMessageModel>{
        val error = ErrorMessageModel(httpStatus = HttpStatus.NOT_FOUND, status = HttpStatus.NOT_FOUND.value(), message = ex.message)
        return ResponseEntity(error, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler
    fun handleTimeInvalidException(ex: TimeInvalidException): ResponseEntity<ErrorMessageModel>{
        val error = ErrorMessageModel(httpStatus = HttpStatus.BAD_REQUEST, status = HttpStatus.BAD_REQUEST.value(), message = ex.message)
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler
    fun handleInvalidRequestBodyException(ex: InvalidRequestBodyException): ResponseEntity<ErrorMessageModel>{
        val error = ErrorMessageModel(httpStatus = HttpStatus.BAD_REQUEST, status = HttpStatus.BAD_REQUEST.value(), message = ex.message)
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler
    fun handleInvalidSensorException(ex: InvalidSensorException): ResponseEntity<ErrorMessageModel>{
        val error = ErrorMessageModel(httpStatus = HttpStatus.BAD_REQUEST, status = HttpStatus.BAD_REQUEST.value(), message = ex.message)
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }
}