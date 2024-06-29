package com.irrigationsystem

import com.irrigationsystem.dto.SensorDtoRequest
import com.irrigationsystem.entity.Sensor
import com.irrigationsystem.exceptions.IdDoesNotExistException
import com.irrigationsystem.exceptions.InvalidRequestBodyException
import com.irrigationsystem.mapper.SensorMapper
import com.irrigationsystem.repository.ISensorRepository
import com.irrigationsystem.service.impl.SensorService
import io.mockk.MockKAnnotations
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import java.util.*

@SpringBootTest
@TestPropertySource("/application-test.properties")
class SensorServiceTest {

    @MockK
    private lateinit var sensorRepository: ISensorRepository
    private lateinit var sensorMapper: SensorMapper
    private lateinit var sensorService: SensorService
    private lateinit var sensorDtoRequest1: SensorDtoRequest
    private lateinit var sensorDtoRequest2: SensorDtoRequest

    @BeforeEach
    fun setUp(){
        MockKAnnotations.init(this)
        sensorService = SensorService(sensorRepository)
        sensorMapper = SensorMapper()
        sensorDtoRequest1 = SensorDtoRequest(sensorName = "Sensor1")
        sensorDtoRequest2 = SensorDtoRequest(sensorName = "Sensor2")
    }

    @AfterEach
    fun tearDown(){
        clearMocks(sensorRepository)
    }

    @Test
    fun testCreateSensor(){
        val sensorExpected = Sensor(id = 1, sensorName = "Sensor1")
        every { sensorRepository.save(sensorMapper.mapDtoRequestToEntity(sensorDtoRequest1)) } returns sensorExpected

        assertEquals(sensorMapper.mapEntityToDtoResponse(sensorExpected), sensorService.createSensor(sensorDtoRequest1))
    }

    @Test
    fun testGetAllSensors(){
        val sensorExpected1 = Sensor(id = 1, sensorName = "Sensor1")
        val sensorExpected2 = Sensor(id = 2, sensorName = "Sensor2")

        every { sensorRepository.findAll() } returns listOf(sensorExpected1, sensorExpected2)

        assertEquals(listOf(sensorExpected1, sensorExpected2).map { sensor -> sensorMapper.mapEntityToDtoResponse(sensor) }
        , sensorService.getAllSensors())
    }

    @Test
    fun testGetSensorById(){
        val sensorExpected = Sensor(id = 1, sensorName = "Sensor1")
        every { sensorRepository.findById(1L) } returns Optional.of(sensorExpected)

        assertEquals(sensorMapper.mapEntityToDtoResponse(sensorExpected), sensorService.getSensorById(1L))
    }

    @Test
    fun testGetSensorByInvalidId(){
        every { sensorRepository.findById(2L) } returns Optional.empty()

        assertThrows<IdDoesNotExistException> { sensorService.getSensorById(2L) }
    }

    @Test
    fun testEditSensorById(){
        val sensorExpected1 = Sensor(id = 1, sensorName = "Sensor1")
        val sensorExpected2 = Sensor(id = 1, sensorName = "Sensor2")
        every { sensorRepository.findById(1L) } returns Optional.of(sensorExpected1)
        every { sensorRepository.save(sensorExpected2) } returns sensorExpected2

        assertEquals(sensorMapper.mapEntityToDtoResponse(sensorExpected2), sensorService.editSensorById(1L, sensorDtoRequest2))
    }

    @Test
    fun testEditSensorByInvalidId(){
        every { sensorRepository.findById(2L) } returns Optional.empty()

        assertThrows<IdDoesNotExistException> { sensorService.editSensorById(2L, sensorDtoRequest1) }
    }

    @Test
    fun testEditSensorWithNullSensorDtoRequest(){
        val sensorExpected1 = Sensor(id = 1, sensorName = "Sensor1")
        every { sensorRepository.findById(1L) } returns Optional.of(sensorExpected1)
        assertThrows<InvalidRequestBodyException> { sensorService.editSensorById(1L, null) }
    }
}