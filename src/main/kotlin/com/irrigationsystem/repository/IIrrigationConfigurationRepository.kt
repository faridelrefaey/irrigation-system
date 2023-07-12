package com.irrigationsystem.repository

import com.irrigationsystem.entity.IrrigationConfiguration
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
interface IIrrigationConfigurationRepository: JpaRepository<IrrigationConfiguration, Long> {

    @Query(value = "SELECT i FROM IrrigationConfiguration i WHERE i.sensor.id = :sensorId AND i.land.id != :landId")
    fun getAllIrrigationConfigurationsForSensorIdAndNotForLandId(@Param("sensorId") sensorId: Long, @Param("landId") landId: Long): List<IrrigationConfiguration>

    @Query(value = "SELECT i FROM IrrigationConfiguration i WHERE i.sensor.id = :sensorId AND i.land.id = :landId")
    fun getAllIrrigationConfigurationForSensorIdAndLandId(@Param("sensorId") sensorId: Long, @Param("landId") landId: Long): List<IrrigationConfiguration>
}