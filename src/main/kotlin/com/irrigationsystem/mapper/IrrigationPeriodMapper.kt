package com.irrigationsystem.mapper

import com.irrigationsystem.dto.IrrigationPeriodDtoResponse
import com.irrigationsystem.entity.IrrigationPeriod

class IrrigationPeriodMapper {

    fun mapEntityToDtoResponse(irrigationPeriod: IrrigationPeriod): IrrigationPeriodDtoResponse{
        return IrrigationPeriodDtoResponse(
            id = irrigationPeriod.id,
            startTime = irrigationPeriod.startTime,
            endTime = irrigationPeriod.endTime,
            isSuccessful = irrigationPeriod.isSuccessful
        )
    }

}