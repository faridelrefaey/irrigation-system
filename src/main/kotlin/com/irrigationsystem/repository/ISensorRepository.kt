package com.irrigationsystem.repository

import com.irrigationsystem.entity.Sensor
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
interface ISensorRepository: JpaRepository<Sensor, Long> {
}