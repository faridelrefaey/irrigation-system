package com.irrigationsystem.dto

import java.time.LocalDateTime

data class IrrigationPeriodDtoRequest(
    var startTime: LocalDateTime? = null,
    var endTime: LocalDateTime? = null,
    var isSuccessful: Boolean = false
)