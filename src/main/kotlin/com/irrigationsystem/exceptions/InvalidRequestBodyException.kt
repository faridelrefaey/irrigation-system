package com.irrigationsystem.exceptions

import java.lang.RuntimeException

class InvalidRequestBodyException(message: String): RuntimeException(message) {
}