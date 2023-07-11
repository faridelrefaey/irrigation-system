package com.irrigationsystem.service

import com.irrigationsystem.dto.SensorDtoRequest
import com.irrigationsystem.dto.SensorDtoResponse
import com.irrigationsystem.entity.Sensor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
interface ISensorService {

    fun createSensor(sensorDtoRequest: SensorDtoRequest): SensorDtoResponse

    fun getAllSensors(): List<SensorDtoResponse>

    fun getSensorById(sensorId: Long): SensorDtoResponse

    fun editSensorById(sensorId: Long, sensorDtoRequest: SensorDtoRequest?): SensorDtoResponse

    fun turnOnSensor(sensorId: Long): Long
}