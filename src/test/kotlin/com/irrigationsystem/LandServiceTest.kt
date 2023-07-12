package com.irrigationsystem

import com.irrigationsystem.dto.LandDtoRequest
import com.irrigationsystem.dto.LandDtoResponse
import com.irrigationsystem.entity.Land
import com.irrigationsystem.exceptions.IdDoesNotExistException
import com.irrigationsystem.exceptions.InvalidRequestBodyException
import com.irrigationsystem.mapper.LandMapper
import com.irrigationsystem.repository.ILandRepository
import com.irrigationsystem.service.ISensorService
import com.irrigationsystem.service.LandService
import com.irrigationsystem.service.SensorService
import io.mockk.MockKAnnotations
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import java.util.*

@SpringBootTest
class LandServiceTest {

    @MockK
    private lateinit var landRepository: ILandRepository
    private lateinit var landService: LandService
    private lateinit var landDtoRequestToBeUsed1: LandDtoRequest
    private lateinit var landForMocking: Land
    private lateinit var landMapper: LandMapper


    @BeforeEach
    fun setUp(){
        MockKAnnotations.init(this)
        landService = LandService(landRepository)
        landDtoRequestToBeUsed1 = LandDtoRequest(seedType = "Corn", landName = "Corn Field", area = 12.5)
        landForMocking = Land(id = 1, seedType = "Corn", landName = "Corn Field", area = 12.5, irrigationConfigurationList = mutableListOf())
        landMapper = LandMapper()
    }

    @AfterEach
    fun tearDown(){
        clearMocks(landRepository)
    }

    @Test
    fun testCreateLand(){
        every { landRepository.save(landMapper.mapDtoRequestToEntity(landDtoRequestToBeUsed1)) } returns landForMocking

        val landDtoResponse: LandDtoResponse = landService.createLand(landDtoRequestToBeUsed1)

        assertEquals(landMapper.mapEntityToDtoResponse(landForMocking), landDtoResponse)
    }

    @Test
    fun testEditLand(){
        val land = Land(id = 1, seedType = "Tomato", landName = "Corn Field", area = 12.5, irrigationConfigurationList = mutableListOf())
        val landDtoRequest = LandDtoRequest(seedType = "Tomato", landName = "Corn Field", area = 12.5)

        every { landRepository.save(land) } returns land
        every { landRepository.findById(1L) } returns Optional.of(land)

        val landDtoResponse: LandDtoResponse = landService.editLand(1L, landDtoRequest)

        assertEquals(landMapper.mapEntityToDtoResponse(land), landDtoResponse)
    }

    @Test
    fun testEditLandWithInvalidLandId(){
        val landDtoRequest = LandDtoRequest(seedType = "Tomato", landName = "Corn Field", area = 12.5)

        every { landRepository.findById(3L) } returns Optional.empty()

        assertThrows<IdDoesNotExistException> { landService.editLand(3L, landDtoRequest) }
    }

    @Test
    fun testEditLandWithNullDtoRequestObject(){
        val land = Land(id = 1, seedType = "Tomato", landName = "Corn Field", area = 12.5, irrigationConfigurationList = mutableListOf())

        every { landRepository.findById(1L) } returns Optional.of(land)

        assertThrows<InvalidRequestBodyException> { landService.editLand(1L, null) }
    }

    @Test
    fun testGetAll(){
        val landList: MutableList<Land> = mutableListOf()
        val landForMocking2 = Land(id = 2, seedType = "Wheat", landName = "Wheat Field", area = 12.5, irrigationConfigurationList = mutableListOf())
        landList.add(landForMocking)
        landList.add(landForMocking2)

        val landDtoResponseList: List<LandDtoResponse> = landList.map { land ->  landMapper.mapEntityToDtoResponse(land) }

        every { landRepository.findAll() } returns landList

        assertEquals(landDtoResponseList, landService.getAllLands())

    }

    @Test
    fun testGetLandById(){
        every { landRepository.findById(1L) } returns Optional.of(landForMocking)

        assertEquals(landMapper.mapEntityToDtoResponse(landForMocking), landService.getLandById(1L))
    }

    @Test
    fun testGetLandByInvalidId(){
        every { landRepository.findById(3L) } returns Optional.empty()

        assertThrows<IdDoesNotExistException> { landService.getLandById(3L) }
    }

    @Test
    fun testDeleteLandById(){
        every { landRepository.findById(1L) } returns Optional.of(landForMocking)
        every { landRepository.deleteById(1L) } returns Unit

        assertEquals("Land with ID 1 deleted successfully.", landService.deleteLandById(1L))
    }

    @Test
    fun testDeleteLandByInvalidId(){
        every { landRepository.findById(3L) } returns Optional.empty()

        assertThrows<IdDoesNotExistException> { landService.deleteLandById(3L) }
    }
}