package com.irrigationsystem.repository

import com.irrigationsystem.entity.IrrigationConfiguration
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
interface IrrigationConfigurationRepository: JpaRepository<IrrigationConfiguration, Long> {
}