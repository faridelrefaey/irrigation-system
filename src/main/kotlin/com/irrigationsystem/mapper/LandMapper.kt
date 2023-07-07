package com.irrigationsystem.mapper

import com.irrigationsystem.dto.LandDtoRequest
import com.irrigationsystem.dto.LandDtoResponse
import com.irrigationsystem.entity.Land

class LandMapper {

    private val irrigationConfigurationMapper = IrrigationConfigurationMapper()

    fun mapEntityToDtoResponse(land: Land): LandDtoResponse{
        return LandDtoResponse(
            id = land.id,
            seedType = land.seedType,
            landName = land.landName,
            area = land.area,
            irrigationConfigurationList = land.irrigationConfigurationList.map { irrigationConfiguration -> irrigationConfigurationMapper.mapEntityToDtoResponse(irrigationConfiguration) }.toMutableList()
        )
    }


    fun mapDtoRequestToEntity(landDtoRequest: LandDtoRequest): Land{
        return Land(
            landName = landDtoRequest.landName,
            area = landDtoRequest.area,
            seedType = landDtoRequest.seedType
        )
    }

//    fun mapDtoResponseToEntity(landDtoResponse: LandDtoResponse): Land{
//        return Land(
//            id = landDtoResponse.id,
//            landName = landDtoResponse.landName,
//            area = landDtoResponse.area,
//            seedType = landDtoResponse.seedType,
//            irrigationConfigurationList = landDtoResponse.irrigationConfigurationList.map { irrigationConfigurationDtoResponse ->  }
//        )
//    }
}