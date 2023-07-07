package com.irrigationsystem.exceptions

import java.lang.RuntimeException

class InvalidSensorException(message: String): RuntimeException(message) {
}