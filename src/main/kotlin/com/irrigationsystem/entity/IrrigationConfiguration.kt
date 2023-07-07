package com.irrigationsystem.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
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
    @OneToOne
    @JoinColumn(name = "sensor_id")
    var sensor: Sensor? = null,
    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "irrigationConfiguration")
    var irrigationPeriodList: MutableList<IrrigationPeriod> = mutableListOf()

)
{
    fun addIrrigationPeriod(irrigationPeriod: IrrigationPeriod){
        irrigationPeriodList.add(irrigationPeriod)
    }
}
