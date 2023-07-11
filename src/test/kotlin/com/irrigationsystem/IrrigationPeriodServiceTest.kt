package com.irrigationsystem

import com.irrigationsystem.entity.IrrigationConfiguration
import com.irrigationsystem.entity.IrrigationPeriod
import com.irrigationsystem.entity.Land
import com.irrigationsystem.entity.Sensor
import com.irrigationsystem.mapper.IrrigationPeriodMapper
import com.irrigationsystem.repository.IIrrigationPeriodRepository
import com.irrigationsystem.service.ISensorService
import com.irrigationsystem.service.IrrigationPeriodService
import io.mockk.MockKAnnotations
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.aspectj.lang.annotation.After
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime

@SpringBootTest
class IrrigationPeriodServiceTest {

    @MockK
    private lateinit var irrigationPeriodRepository: IIrrigationPeriodRepository
    @MockK
    private lateinit var sensorService: ISensorService
    private lateinit var irrigationPeriodService: IrrigationPeriodService
    private lateinit var irrigationPeriod: IrrigationPeriod
    private lateinit var landForMocking: Land
    private lateinit var sensorForMocking: Sensor
    private lateinit var irrigationPeriodMapper: IrrigationPeriodMapper


    @BeforeEach
    fun setUp(){
        MockKAnnotations.init(this)
        irrigationPeriodService = IrrigationPeriodService(irrigationPeriodRepository, sensorService)
        irrigationPeriod = IrrigationPeriod(startTime = LocalDateTime.parse("2023-08-01T00:00:00"))
        landForMocking = Land(id = 1, seedType = "Corn", landName = "Corn Field", area = 12.5, irrigationConfigurationList = mutableListOf())
        sensorForMocking = Sensor(id = 1, sensorName = "Sensor1")
        irrigationPeriodMapper = IrrigationPeriodMapper()
    }

    @AfterEach
    fun tearDown(){
        clearMocks(irrigationPeriodRepository, sensorService)
    }

    @Test
    fun testCreateIrrigationPeriod(){
        val irrigationPeriodExpected = IrrigationPeriod(id = 1, startTime = LocalDateTime.parse("2023-08-01T00:00:00"))

        every { irrigationPeriodRepository.save(irrigationPeriod) } returns irrigationPeriodExpected

        assertEquals(irrigationPeriodExpected, irrigationPeriodService.createIrrigationPeriod(irrigationPeriod))
    }

    @Test
    fun testGetIrrigationPeriodsForIrrigationConfigurationId(){
        val irrigationPeriodExpected = IrrigationPeriod(id = 1, startTime = LocalDateTime.parse("2023-08-01T00:00:00"))

        every { irrigationPeriodRepository.getAllIrrigationPeriodsForIrrigationConfigurationId(1) } returns listOf(irrigationPeriodExpected)

        assertEquals(listOf(irrigationPeriodExpected).map { irrigationPeriod -> irrigationPeriodMapper.mapEntityToDtoResponse(irrigationPeriod) }
        , irrigationPeriodService.getAllIrrigationPeriodsForIrrigationConfigurationId(1L))
    }
}