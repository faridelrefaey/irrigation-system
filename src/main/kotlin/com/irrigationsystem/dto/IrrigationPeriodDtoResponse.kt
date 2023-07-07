package com.irrigationsystem.dto

import java.time.LocalDateTime

data class IrrigationPeriodDtoResponse(
    var id: Long? = null,
    var startTime: LocalDateTime? = null,
    var endTime: LocalDateTime? = null,
    var isSuccessful: Boolean = false
)
