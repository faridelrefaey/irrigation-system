package com.irrigationsystem.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.time.LocalDateTime

@Entity
data class IrrigationPeriod(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var startTime: LocalDateTime? = null,
    var endTime: LocalDateTime? = null,
    var isSuccessful: Boolean = false,
    @ManyToOne
    @JoinColumn(name = "irrigation_configuration_id")
    var irrigationConfiguration: IrrigationConfiguration? = null
)
