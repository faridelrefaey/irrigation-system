package com.irrigationsystem.mapper

import com.irrigationsystem.dto.IrrigationConfigurationDtoRequest
import com.irrigationsystem.dto.IrrigationConfigurationDtoResponse
import com.irrigationsystem.entity.IrrigationConfiguration
import com.irrigationsystem.security.entity.User

class IrrigationConfigurationMapper {

    private val sensorMapper = SensorMapper()
    private val irrigationPeriodMapper = IrrigationPeriodMapper()

    fun mapEntityToDtoResponse(irrigationConfiguration: IrrigationConfiguration): IrrigationConfigurationDtoResponse{
        return IrrigationConfigurationDtoResponse(
            id = irrigationConfiguration.id,
            startDate = irrigationConfiguration.startDate,
            endDate = irrigationConfiguration.endDate,
            timesToWaterDuringInterval = irrigationConfiguration.timesToWaterDuringInterval,
            waterAmount = irrigationConfiguration.waterAmount,
            sensor = irrigationConfiguration.sensor?.let { sensorMapper.mapEntityToDtoResponse(it) },
            irrigationPeriodList = irrigationConfiguration.irrigationPeriodList.map { irrigationPeriod -> irrigationPeriodMapper.mapEntityToDtoResponse(irrigationPeriod) }.toMutableList()
        )
    }

    fun mapDtoRequestToEntity(irrigationConfigurationDtoRequest: IrrigationConfigurationDtoRequest, user: User): IrrigationConfiguration{
        return IrrigationConfiguration(
            startDate = irrigationConfigurationDtoRequest.startDate,
            endDate = irrigationConfigurationDtoRequest.endDate,
            timesToWaterDuringInterval = irrigationConfigurationDtoRequest.timesToWaterDuringInterval,
            waterAmount = irrigationConfigurationDtoRequest.waterAmount,
            user = user
        )
    }


}