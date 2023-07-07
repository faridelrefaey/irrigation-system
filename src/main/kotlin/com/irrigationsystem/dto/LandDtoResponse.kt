package com.irrigationsystem.dto

import com.irrigationsystem.entity.IrrigationConfiguration
import jakarta.persistence.*
import java.time.LocalDateTime

data class LandDtoResponse(
    var id: Long? = null,
    var seedType: String? = null,
    var landName: String? = null,
    var area: Double? = null,
    var irrigationConfigurationList: MutableList<IrrigationConfigurationDtoResponse> = mutableListOf()
)