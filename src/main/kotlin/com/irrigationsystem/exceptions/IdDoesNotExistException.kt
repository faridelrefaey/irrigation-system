package com.irrigationsystem.exceptions

import java.lang.RuntimeException

class IdDoesNotExistException(message: String): RuntimeException(message) {
}