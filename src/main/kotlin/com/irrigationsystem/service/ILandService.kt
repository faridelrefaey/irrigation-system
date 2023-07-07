package com.irrigationsystem.service

import com.irrigationsystem.dto.LandDtoRequest
import com.irrigationsystem.dto.LandDtoResponse
import org.springframework.web.bind.annotation.PathVariable

interface ILandService {

    fun createLand(landDtoRequest: LandDtoRequest): LandDtoResponse

    fun editLand(landId: Long, landDtoRequest: LandDtoRequest?): LandDtoResponse

    fun getAllLands(): List<LandDtoResponse>

    fun getLandById(landId: Long): LandDtoResponse

    fun deleteLandById(landId: Long): String

}