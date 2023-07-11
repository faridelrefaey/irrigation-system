package com.irrigationsystem.service

import com.irrigationsystem.dto.SensorDtoRequest
import com.irrigationsystem.dto.SensorDtoResponse
import com.irrigationsystem.entity.Sensor
import com.irrigationsystem.exceptions.IdDoesNotExistException
import com.irrigationsystem.exceptions.InvalidRequestBodyException
import com.irrigationsystem.mapper.SensorMapper
import com.irrigationsystem.repository.ISensorRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.time.LocalDateTime
import java.util.Optional
import kotlin.math.log

@Service
@Transactional

class SensorService(
    @Autowired private val sensorRepository: ISensorRepository
): ISensorService {

    private val sensorMapper = SensorMapper()
    private val logger: Logger = LoggerFactory.getLogger(javaClass)


    override fun createSensor(sensorDtoRequest: SensorDtoRequest): SensorDtoResponse {
        return sensorMapper.mapEntityToDtoResponse(sensorRepository.save(sensorMapper.mapDtoRequestToEntity(sensorDtoRequest)))
    }

    override fun getAllSensors(): List<SensorDtoResponse> {
        return sensorRepository.findAll().map { sensor -> sensorMapper.mapEntityToDtoResponse(sensor) }
    }

    override fun getSensorById(sensorId: Long): SensorDtoResponse {
        val optionalSensor: Optional<Sensor> = sensorRepository.findById(sensorId)

        if(optionalSensor.isEmpty){
            throw IdDoesNotExistException("No sensor with the given ID was found")
        }
        return sensorMapper.mapEntityToDtoResponse(optionalSensor.get())
    }

    override fun editSensorById(sensorId: Long, sensorDtoRequest: SensorDtoRequest?): SensorDtoResponse {
        val optionalSensor: Optional<Sensor> = sensorRepository.findById(sensorId)

        if(optionalSensor.isEmpty){
            throw IdDoesNotExistException("No sensor with the given ID was found")
        }

        if(sensorDtoRequest == null){
            throw InvalidRequestBodyException("Please add a sensor request body.")
        }

        optionalSensor.get().sensorName = sensorDtoRequest.sensorName
        return sensorMapper.mapEntityToDtoResponse(sensorRepository.save(optionalSensor.get()))
    }

    override fun turnOnSensor(sensorId: Long): Long {
        //the below if condition is just to mock the retry attempts
        val startTime: LocalDateTime = LocalDateTime.now()
        if(sensorId == 1L) {
            logger.info("Turing on sensor with ID $sensorId")
            Thread.sleep(5000)
            val endTime: LocalDateTime = LocalDateTime.now()
            return Duration.between(startTime, endTime).toMillis()
        }

        logger.info("Error reaching out to sensor with ID $sensorId")
        Thread.sleep(5000)
        return 0
    }


}