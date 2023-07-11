package com.irrigationsystem.repository

import com.irrigationsystem.entity.IrrigationConfiguration
import com.irrigationsystem.entity.IrrigationPeriod
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Transactional
@Repository
interface IIrrigationPeriodRepository: JpaRepository<IrrigationPeriod, Long> {

    @Query(value = "SELECT i FROM IrrigationPeriod i WHERE i.isSuccessful = false")
    fun getAllIrrigationPeriodWithIsSuccessfulFalse(): List<IrrigationPeriod>

    @Query(value = "SELECT i FROM IrrigationPeriod i WHERE i.irrigationConfiguration.id = :id")
    fun getAllIrrigationPeriodsForIrrigationConfigurationId(@Param("id") irrigationConfigurationId: Long): List<IrrigationPeriod>

}