package com.irrigationsystem.dto

import java.time.LocalDateTime


data class IrrigationConfigurationDtoResponse(
    var id: Long? = null,
    var startDate: LocalDateTime? = null,
    var endDate: LocalDateTime? = null,
    var timesToWaterDuringInterval: Long? = null,
    var waterAmount: Double? = null,
    var sensor: SensorDtoResponse? = null,
    var irrigationPeriodList: MutableList<IrrigationPeriodDtoResponse> = mutableListOf()
)