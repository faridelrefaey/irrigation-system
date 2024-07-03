package com.irrigationsystem.service.impl

import com.irrigationsystem.dto.LandDtoRequest
import com.irrigationsystem.dto.LandDtoResponse
import com.irrigationsystem.entity.Land
import com.irrigationsystem.exceptions.IrrigationSystemException
import com.irrigationsystem.mapper.LandMapper
import com.irrigationsystem.repository.ILandRepository
import com.irrigationsystem.security.repository.UserRepository
import com.irrigationsystem.security.utils.JwtUtils
import com.irrigationsystem.service.ILandService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class LandService(
    @Autowired private val landRepository: ILandRepository,
    @Autowired private val userRepository: UserRepository,
) : ILandService {

    private val landMapper = LandMapper()

    override fun createLand(landDtoRequest: LandDtoRequest): LandDtoResponse {
        val username = JwtUtils.getUsernameFromToken()
        val user = userRepository.findByUsername(username)
        return landMapper.mapEntityToDtoResponse(
            landRepository.save(
                landMapper.mapDtoRequestToEntity(
                    landDtoRequest,
                    user!!
                )
            )
        )
    }

    override fun editLand(landId: Long, landDtoRequest: LandDtoRequest?): LandDtoResponse {
        val username = JwtUtils.getUsernameFromToken()
        val user = userRepository.findByUsername(username)

        val optionalLand: Land = landRepository.findLandById(landId, user!!)
            ?: throw IrrigationSystemException("No land with the given ID was found.")

        if (landDtoRequest == null) {
            throw IrrigationSystemException("Please add a land request body")
        }

        val land: Land = optionalLand
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
        val username = JwtUtils.getUsernameFromToken()
        val userId = userRepository.findByUsername(username)!!.id
        return landRepository.findByUserId(userId!!).map { land -> landMapper.mapEntityToDtoResponse(land) }
    }

    override fun getLandById(landId: Long): LandDtoResponse {
        val username = JwtUtils.getUsernameFromToken()
        val user = userRepository.findByUsername(username)

        val optionalLand: Land = landRepository.findLandById(landId, user!!)
            ?: throw IrrigationSystemException("No land with the given ID was found.")

        return landMapper.mapEntityToDtoResponse(optionalLand)
    }

    override fun deleteLandById(landId: Long): String {
        val username = JwtUtils.getUsernameFromToken()
        val user = userRepository.findByUsername(username)

        val optionalLand: Land = landRepository.findLandById(landId, user!!)
            ?: throw IrrigationSystemException("No land with the given ID was found.")

        landRepository.deleteById(optionalLand.id!!)
        return "Land with ID $landId deleted successfully."
    }

}