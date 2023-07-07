package com.irrigationsystem.mapper

import com.irrigationsystem.dto.IrrigationConfigurationDtoRequest
import com.irrigationsystem.dto.IrrigationConfigurationDtoResponse
import com.irrigationsystem.entity.IrrigationConfiguration

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

    fun mapDtoRequestToEntity(irrigationConfigurationDtoRequest: IrrigationConfigurationDtoRequest): IrrigationConfiguration{
        return IrrigationConfiguration(
            startDate = irrigationConfigurationDtoRequest.startDate,
            endDate = irrigationConfigurationDtoRequest.endDate,
            timesToWaterDuringInterval = irrigationConfigurationDtoRequest.timesToWaterDuringInterval,
            waterAmount = irrigationConfigurationDtoRequest.waterAmount
        )
    }


}