package com.irrigationsystem.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
data class IrrigationConfiguration(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var startDate: LocalDateTime? = null,
    var endDate: LocalDateTime? = null,
    var timesToWaterDuringInterval: Long? = null,
    var waterAmount: Double? = null,
    @ManyToOne
    @JoinColumn(name = "land_id")
    var land: Land? = null,
    @ManyToOne
    @JoinColumn(name = "sensor_id")
    var sensor: Sensor? = null,
    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "irrigationConfiguration", fetch = FetchType.EAGER)
    var irrigationPeriodList: MutableList<IrrigationPeriod> = mutableListOf()

)
