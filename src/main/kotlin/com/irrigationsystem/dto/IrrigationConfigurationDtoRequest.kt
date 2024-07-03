package com.irrigationsystem.dto

import java.time.LocalDateTime

data class IrrigationConfigurationDtoRequest(
    var startDate: LocalDateTime? = null,
    var endDate: LocalDateTime? = null,
    var timesToWaterDuringInterval: Long? = null,
    var waterAmount: Double? = null,
    var landId: Long? = null,
    var sensorId: Long? = null
)