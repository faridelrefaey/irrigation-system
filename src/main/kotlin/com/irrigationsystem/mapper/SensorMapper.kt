package com.irrigationsystem.mapper

import com.irrigationsystem.dto.SensorDtoRequest
import com.irrigationsystem.dto.SensorDtoResponse
import com.irrigationsystem.entity.Sensor

class SensorMapper {

    fun mapDtoRequestToEntity(sensorDtoRequest: SensorDtoRequest): Sensor{
        return Sensor(
            sensorName = sensorDtoRequest.sensorName
        )
    }

    fun mapEntityToDtoResponse(sensor: Sensor): SensorDtoResponse{
        return SensorDtoResponse(
            id = sensor.id,
            sensorName = sensor.sensorName
        )
    }

//    fun mapDtoResponseToEntity(sensorDtoResponse: SensorDtoResponse): Sensor{
//        return Sensor(
//            id = sensorDtoResponse.id,
//            sensorName = sensorDtoResponse.sensorName
//        )
//    }
}