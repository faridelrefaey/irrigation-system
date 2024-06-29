package com.irrigationsystem.service.impl

import com.irrigationsystem.dto.LandDtoRequest
import com.irrigationsystem.dto.LandDtoResponse
import com.irrigationsystem.entity.Land
import com.irrigationsystem.exceptions.IdDoesNotExistException
import com.irrigationsystem.exceptions.InvalidRequestBodyException
import com.irrigationsystem.mapper.LandMapper
import com.irrigationsystem.repository.ILandRepository
import com.irrigationsystem.service.ILandService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class LandService(
    @Autowired private val landRepository: ILandRepository
) : ILandService {

    private val landMapper = LandMapper()

    override fun createLand(landDtoRequest: LandDtoRequest): LandDtoResponse {
        return landMapper.mapEntityToDtoResponse(
            landRepository.save(
                landMapper.mapDtoRequestToEntity(
                    landDtoRequest
                )
            )
        )
    }

    override fun editLand(landId: Long, landDtoRequest: LandDtoRequest?): LandDtoResponse {
        val optionalLand: Optional<Land> = landRepository.findById(landId)
        if (optionalLand.isEmpty) {
            throw IdDoesNotExistException("No land with the given ID was found.")
        }

        if (landDtoRequest == null) {
            throw InvalidRequestBodyException("Please add a land request body")
        }

        val land: Land = optionalLand.get()
        if (landDtoRequest.landName != null) {
            land.landName = landDtoRequest.landName
        }
        if (landDtoRequest.area != null) {
            land.area = landDtoRequest.area
        }
        if (landDtoRequest.seedType != null) {
            land.seedType = landDtoRequest.seedType
        }

        val savedLand: Land = landRepository.save(land)

        return landMapper.mapEntityToDtoResponse(savedLand)
    }

    override fun getAllLands(): List<LandDtoResponse> {
        return landRepository.findAll().map { land -> landMapper.mapEntityToDtoResponse(land) }
    }

    override fun getLandById(landId: Long): LandDtoResponse {
        val optionalLand: Optional<Land> = landRepository.findById(landId)
        if (optionalLand.isEmpty) {
            throw IdDoesNotExistException("No land with the given ID was found.")
        }

        return landMapper.mapEntityToDtoResponse(optionalLand.get())
    }

    override fun deleteLandById(landId: Long): String {
        val optionalLand: Optional<Land> = landRepository.findById(landId)
        if (optionalLand.isEmpty) {
            throw IdDoesNotExistException("No land with the given ID was found.")
        }

        landRepository.deleteById(landId)
        return "Land with ID $landId deleted successfully."
    }

}