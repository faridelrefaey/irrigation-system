package com.irrigationsystem.service

import com.irrigationsystem.dto.LandDtoRequest
import com.irrigationsystem.dto.LandDtoResponse
import com.irrigationsystem.entity.Land
import com.irrigationsystem.exceptions.IdDoesNotExistException
import com.irrigationsystem.exceptions.InvalidRequestBodyException
import com.irrigationsystem.mapper.LandMapper
import com.irrigationsystem.repository.ILandRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class LandService(
    @Autowired private val landRepository: ILandRepository,
    @Autowired private val sensorService: ISensorService
): ILandService {

    private val landMapper = LandMapper()
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun createLand(landDtoRequest: LandDtoRequest): LandDtoResponse {
        val landToBeSaved: Land = landMapper.mapDtoRequestToEntity(landDtoRequest)
        val savedLand: Land = landRepository.save(landToBeSaved)

        return landMapper.mapEntityToDtoResponse(savedLand)
    }

    override fun editLand(landId: Long, landDtoRequest: LandDtoRequest?): LandDtoResponse {
        val optionalLand: Optional<Land> = landRepository.findById(landId)
        if(optionalLand.isEmpty){
            throw IdDoesNotExistException("No land with the given ID was found.")
        }

        if(landDtoRequest == null){
            throw InvalidRequestBodyException("Please add a land request body")
        }

        val land: Land = optionalLand.get()
        if(landDtoRequest.landName != null) {
            land.landName = landDtoRequest.landName
        }
        if(landDtoRequest.area != null) {
            land.area = landDtoRequest.area
        }
        if(landDtoRequest.seedType != null){
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
        if(optionalLand.isEmpty){
            throw IdDoesNotExistException("No land with the given ID was found.")
        }

        return landMapper.mapEntityToDtoResponse(optionalLand.get())
    }

    override fun deleteLandById(landId: Long): String {
        val optionalLand: Optional<Land> = landRepository.findById(landId)
        if(optionalLand.isEmpty){
            throw IdDoesNotExistException("No land with the given ID was found.")
        }

        landRepository.deleteById(landId)
        return "Land with ID $landId deleted successfully."
    }

}