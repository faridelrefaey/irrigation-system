package com.irrigationsystem

import com.irrigationsystem.dto.IrrigationConfigurationDtoRequest
import com.irrigationsystem.entity.IrrigationConfiguration
import com.irrigationsystem.entity.IrrigationPeriod
import com.irrigationsystem.entity.Land
import com.irrigationsystem.entity.Sensor
import com.irrigationsystem.exceptions.IdDoesNotExistException
import com.irrigationsystem.exceptions.InvalidRequestBodyException
import com.irrigationsystem.exceptions.InvalidSensorException
import com.irrigationsystem.exceptions.InvalidTimeException
import com.irrigationsystem.mapper.IrrigationConfigurationMapper
import com.irrigationsystem.mapper.IrrigationPeriodMapper
import com.irrigationsystem.repository.ILandRepository
import com.irrigationsystem.repository.ISensorRepository
import com.irrigationsystem.repository.IIrrigationConfigurationRepository
import com.irrigationsystem.service.IrrigationConfigurationService
import com.irrigationsystem.service.IrrigationPeriodService
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
import java.time.LocalDateTime
import java.util.*

@SpringBootTest
@TestPropertySource("/application-test.properties")
class IrrigationConfigurationServiceTest {

    @MockK
    private lateinit var IIrrigationConfigurationRepository: IIrrigationConfigurationRepository
    @MockK
    private lateinit var landRepository: ILandRepository
    @MockK
    private lateinit var sensorRepository: ISensorRepository
    @MockK
    private lateinit var irrigationPeriodService: IrrigationPeriodService
    private lateinit var irrigationConfigurationService: IrrigationConfigurationService
    private lateinit var irrigationConfigurationDtoRequestToBeUsed1: IrrigationConfigurationDtoRequest
    private lateinit var landForMocking: Land
    private lateinit var sensorForMocking: Sensor
    private lateinit var irrigationConfigurationMapper: IrrigationConfigurationMapper
    private lateinit var irrigationPeriodMapper: IrrigationPeriodMapper

    @BeforeEach
    fun setUp(){
        MockKAnnotations.init(this)
        irrigationConfigurationService = IrrigationConfigurationService(IIrrigationConfigurationRepository, landRepository, sensorRepository, irrigationPeriodService)
        landForMocking = Land(id = 1, seedType = "Corn", landName = "Corn Field", area = 12.5, irrigationConfigurationList = mutableListOf())
        sensorForMocking = Sensor(id = 1, sensorName = "Sensor1")
        irrigationConfigurationDtoRequestToBeUsed1 = IrrigationConfigurationDtoRequest(
            startDate = LocalDateTime.parse("2023-08-01T00:00:00.0"),
            endDate = LocalDateTime.parse("2023-08-05T15:00:00.0"),
            timesToWaterDuringInterval = 3,
            waterAmount = 12.5,
            landId = 1,
            sensorId = 1
        )

        irrigationConfigurationMapper = IrrigationConfigurationMapper()

        irrigationPeriodMapper = IrrigationPeriodMapper()
    }

    @AfterEach
    fun tearDown(){
        clearMocks(IIrrigationConfigurationRepository, landRepository, sensorRepository, irrigationPeriodService)
    }


    @Test
    fun testCreateIrrigationConfiguration(){
        val irrigationPeriodExpected1 = IrrigationPeriod(id = 1L, startTime = LocalDateTime.parse("2023-08-01T00:00:00"))
        val irrigationPeriodExpected2 = IrrigationPeriod(id = 2L, startTime = LocalDateTime.parse("2023-08-02T13:00:00"))
        val irrigationPeriodExpected3 = IrrigationPeriod(id = 3L, startTime = LocalDateTime.parse("2023-08-04T02:00:00"))
        val irrigationConfigurationExpected = IrrigationConfiguration(
            id = 1,
            startDate = LocalDateTime.parse("2023-08-01T00:00:00.0"),
            endDate = LocalDateTime.parse("2023-08-05T15:00:00.0"),
            timesToWaterDuringInterval = 3,
            waterAmount = 12.5,
            land = landForMocking,
            sensor = sensorForMocking,
            irrigationPeriodList = mutableListOf(irrigationPeriodExpected1, irrigationPeriodExpected2, irrigationPeriodExpected3)
        )
        val irrigationConfigurationToBeSaved = IrrigationConfiguration(
            id = null,
            startDate = LocalDateTime.parse("2023-08-01T00:00:00.0"),
            endDate = LocalDateTime.parse("2023-08-05T15:00:00.0"),
            timesToWaterDuringInterval = 3,
            waterAmount = 12.5,
            land = landForMocking,
            sensor = sensorForMocking
        )


        val irrigationPeriodToBeSaved1 = IrrigationPeriod(startTime = LocalDateTime.parse("2023-08-01T00:00:00"), irrigationConfiguration = irrigationConfigurationToBeSaved)
        val irrigationPeriodToBeSaved2 = IrrigationPeriod(startTime = LocalDateTime.parse("2023-08-02T13:00:00"), irrigationConfiguration = irrigationConfigurationToBeSaved)
        val irrigationPeriodToBeSaved3 = IrrigationPeriod(startTime = LocalDateTime.parse("2023-08-04T02:00:00"), irrigationConfiguration = irrigationConfigurationToBeSaved)

        every { landRepository.findById(1L) } returns Optional.of(landForMocking)
        every { sensorRepository.findById(1L) } returns Optional.of(sensorForMocking)
        every { IIrrigationConfigurationRepository.save(irrigationConfigurationToBeSaved) } returns irrigationConfigurationExpected
        every { irrigationPeriodService.createIrrigationPeriod(irrigationPeriodToBeSaved1) } returns irrigationPeriodExpected1
        every { irrigationPeriodService.createIrrigationPeriod(irrigationPeriodToBeSaved2) } returns irrigationPeriodExpected2
        every { irrigationPeriodService.createIrrigationPeriod(irrigationPeriodToBeSaved3) } returns irrigationPeriodExpected3
        every { IIrrigationConfigurationRepository.getAllIrrigationConfigurationForSensorIdAndLandId(1L, 1L) } returns listOf()
        every { IIrrigationConfigurationRepository.getAllIrrigationConfigurationsForSensorIdAndNotForLandId(1L, 1L) } returns listOf()
        every { irrigationPeriodService.getAllIrrigationPeriodsForIrrigationConfigurationId(1L) } returns listOf(irrigationPeriodExpected1, irrigationPeriodExpected2, irrigationPeriodExpected3).map { irrigationPeriod -> irrigationPeriodMapper.mapEntityToDtoResponse(irrigationPeriod) }


        assertEquals((irrigationConfigurationMapper.mapEntityToDtoResponse(irrigationConfigurationExpected)).irrigationPeriodList, (irrigationConfigurationService.createIrrigationConfiguration(irrigationConfigurationDtoRequestToBeUsed1)).irrigationPeriodList)
    }

    @Test
    fun testCreateIrrigationConfigurationWithNullSensorIdAndLandId(){
        val irrigationConfigurationDtoRequestNullSensorId = IrrigationConfigurationDtoRequest(
            startDate = LocalDateTime.parse("2023-08-07T00:00:00.0"),
            endDate = LocalDateTime.parse("2023-08-08T15:00:00.0"),
            timesToWaterDuringInterval = 3,
            waterAmount = 25.0,
            landId = 1,
            sensorId = null
        )

        val irrigationConfigurationDtoRequestNullLandId = IrrigationConfigurationDtoRequest(
            startDate = LocalDateTime.parse("2023-08-07T00:00:00.0"),
            endDate = LocalDateTime.parse("2023-08-08T15:00:00.0"),
            timesToWaterDuringInterval = 3,
            waterAmount = 25.0,
            landId = null,
            sensorId = 1
        )

        assertThrows<InvalidRequestBodyException> { irrigationConfigurationService.createIrrigationConfiguration(irrigationConfigurationDtoRequestNullSensorId) }
        assertThrows<InvalidRequestBodyException> { irrigationConfigurationService.createIrrigationConfiguration(irrigationConfigurationDtoRequestNullLandId) }

    }

    @Test
    fun testCreateIrrigationConfigurationWithNullStartDateAndNullEndDate(){
        val irrigationConfigurationDtoRequestNullStartDate = IrrigationConfigurationDtoRequest(
            startDate = null,
            endDate = LocalDateTime.parse("2023-08-08T15:00:00.0"),
            timesToWaterDuringInterval = 3,
            waterAmount = 25.0,
            landId = 1,
            sensorId = 1
        )

        val irrigationConfigurationDtoRequestNullEndDate = IrrigationConfigurationDtoRequest(
            startDate = LocalDateTime.parse("2023-08-07T00:00:00.0"),
            endDate = null,
            timesToWaterDuringInterval = 3,
            waterAmount = 25.0,
            landId = 1,
            sensorId = 1
        )

        assertThrows<InvalidRequestBodyException> { irrigationConfigurationService.createIrrigationConfiguration(irrigationConfigurationDtoRequestNullStartDate) }
        assertThrows<InvalidRequestBodyException> { irrigationConfigurationService.createIrrigationConfiguration(irrigationConfigurationDtoRequestNullEndDate) }
    }

    @Test
    fun testCreateIrrigationConfigurationWithNullTimesToWater(){
        val irrigationConfigurationDtoRequestNullTimesToWater = IrrigationConfigurationDtoRequest(
            startDate = LocalDateTime.parse("2023-08-07T00:00:00.0"),
            endDate = LocalDateTime.parse("2023-08-08T15:00:00.0"),
            timesToWaterDuringInterval = null,
            waterAmount = 25.0,
            landId = 1,
            sensorId = 1
        )
        assertThrows<InvalidRequestBodyException> { irrigationConfigurationService.createIrrigationConfiguration(irrigationConfigurationDtoRequestNullTimesToWater) }
    }

    @Test
    fun testCreateIrrigationConfigurationWithNullWaterAmount(){
        val irrigationConfigurationDtoRequestNullWaterAmount = IrrigationConfigurationDtoRequest(
            startDate = LocalDateTime.parse("2023-08-07T00:00:00.0"),
            endDate = LocalDateTime.parse("2023-08-08T15:00:00.0"),
            timesToWaterDuringInterval = 3,
            waterAmount = null,
            landId = 1,
            sensorId = 1
        )

        assertThrows<InvalidRequestBodyException> { irrigationConfigurationService.createIrrigationConfiguration(irrigationConfigurationDtoRequestNullWaterAmount) }

    }

    @Test
    fun testCreateIrrigationConfigurationWithStartDateGreaterThanEndDate(){
        val irrigationConfigurationDtoRequest1 = IrrigationConfigurationDtoRequest(
            startDate = LocalDateTime.parse("2023-08-10T00:00:00.0"),
            endDate = LocalDateTime.parse("2023-08-08T15:00:00.0"),
            timesToWaterDuringInterval = 3,
            waterAmount = 25.0,
            landId = 1,
            sensorId = 1
        )
        assertThrows<InvalidTimeException> { irrigationConfigurationService.createIrrigationConfiguration(irrigationConfigurationDtoRequest1) }
    }

    @Test
    fun testCreateIrrigationConfigurationWithStartDateInThePast(){
        val irrigationConfigurationDtoRequest1 = IrrigationConfigurationDtoRequest(
            startDate = LocalDateTime.parse("2023-07-10T00:00:00.0"),
            endDate = LocalDateTime.parse("2023-08-10T15:00:00.0"),
            timesToWaterDuringInterval = 3,
            waterAmount = 25.0,
            landId = 1,
            sensorId = 1
        )

        assertThrows<InvalidTimeException> { irrigationConfigurationService.createIrrigationConfiguration(irrigationConfigurationDtoRequest1) }
    }

    @Test
    fun testCreateIrrigationConfigurationWithLandIdAndSensorId(){
        val irrigationConfigurationDtoRequestNullWaterAmount = IrrigationConfigurationDtoRequest(
            startDate = LocalDateTime.parse("2023-08-07T00:00:00.0"),
            endDate = LocalDateTime.parse("2023-08-08T15:00:00.0"),
            timesToWaterDuringInterval = 3,
            waterAmount = 12.5,
            landId = 2,
            sensorId = 2
        )

        every { landRepository.findById(2L) } returns Optional.empty()
        every { sensorRepository.findById(2L) } returns Optional.empty()

        assertThrows<IdDoesNotExistException> { irrigationConfigurationService.createIrrigationConfiguration(irrigationConfigurationDtoRequestNullWaterAmount) }
    }

    @Test
    fun testCreateIrrigationConfigurationForABusySensor(){
        val irrigationPeriodExpected1 = IrrigationPeriod(id = 1L, startTime = LocalDateTime.parse("2023-08-01T00:00:00"))
        val irrigationPeriodExpected2 = IrrigationPeriod(id = 2L, startTime = LocalDateTime.parse("2023-08-02T13:00:00"))
        val irrigationPeriodExpected3 = IrrigationPeriod(id = 3L, startTime = LocalDateTime.parse("2023-08-04T02:00:00"))
        val land: Land = Land(id = 2L, seedType = "a", landName = "a", area = 12.5)
        val irrigationConfigurationExpected = IrrigationConfiguration(
            id = 1,
            startDate = LocalDateTime.parse("2023-08-01T00:00:00.0"),
            endDate = LocalDateTime.parse("2023-08-05T15:00:00.0"),
            timesToWaterDuringInterval = 3,
            waterAmount = 12.5,
            land = land,
            sensor = sensorForMocking,
            irrigationPeriodList = mutableListOf(irrigationPeriodExpected1, irrigationPeriodExpected2, irrigationPeriodExpected3)
        )

        every { landRepository.findById(1L) } returns Optional.of(landForMocking)
        every { sensorRepository.findById(1L) } returns Optional.of(sensorForMocking)
        every { IIrrigationConfigurationRepository.getAllIrrigationConfigurationsForSensorIdAndNotForLandId(1L, 1L) } returns listOf(irrigationConfigurationExpected)
        assertThrows<InvalidSensorException> { irrigationConfigurationService.createIrrigationConfiguration(irrigationConfigurationDtoRequestToBeUsed1) }
    }

    @Test
    fun testCreateIrrigationConfigurationForAnIntersectingDateRangeWithSameLandAndSensor(){
        val irrigationPeriodExpected1 = IrrigationPeriod(id = 1L, startTime = LocalDateTime.parse("2023-08-01T00:00:00"))
        val irrigationPeriodExpected2 = IrrigationPeriod(id = 2L, startTime = LocalDateTime.parse("2023-08-02T13:00:00"))
        val irrigationPeriodExpected3 = IrrigationPeriod(id = 3L, startTime = LocalDateTime.parse("2023-08-04T02:00:00"))
        val irrigationConfigurationExpected = IrrigationConfiguration(
            id = 1,
            startDate = LocalDateTime.parse("2023-08-01T00:00:00.0"),
            endDate = LocalDateTime.parse("2023-08-05T15:00:00.0"),
            timesToWaterDuringInterval = 3,
            waterAmount = 12.5,
            land = landForMocking,
            sensor = sensorForMocking,
            irrigationPeriodList = mutableListOf(irrigationPeriodExpected1, irrigationPeriodExpected2, irrigationPeriodExpected3)
        )

        val irrigationConfigurationDtoRequest = IrrigationConfigurationDtoRequest(
            startDate = LocalDateTime.parse("2023-08-02T00:00:00.0"),
            endDate = LocalDateTime.parse("2023-08-08T15:00:00.0"),
            timesToWaterDuringInterval = 3,
            waterAmount = 12.5,
            landId = 1,
            sensorId = 1
        )

        every { landRepository.findById(1L) } returns Optional.of(landForMocking)
        every { sensorRepository.findById(1L) } returns Optional.of(sensorForMocking)
        every { IIrrigationConfigurationRepository.getAllIrrigationConfigurationsForSensorIdAndNotForLandId(1L, 1L) } returns listOf()
        every { IIrrigationConfigurationRepository.getAllIrrigationConfigurationForSensorIdAndLandId(1L, 1L) } returns listOf(irrigationConfigurationExpected)
        assertThrows<InvalidSensorException> { irrigationConfigurationService.createIrrigationConfiguration(irrigationConfigurationDtoRequest) }
    }

    @Test
    fun testGetAllIrrigationConfiguration(){
        val irrigationConfigurationExpected1 = IrrigationConfiguration(
            id = 1,
            startDate = LocalDateTime.parse("2023-08-01T00:00:00.0"),
            endDate = LocalDateTime.parse("2023-08-05T15:00:00.0"),
            timesToWaterDuringInterval = 3,
            waterAmount = 12.5,
            land = landForMocking,
            sensor = sensorForMocking
        )

        val irrigationConfigurationExpected2 = IrrigationConfiguration(
            id = 2,
            startDate = LocalDateTime.parse("2023-08-01T00:00:00.0"),
            endDate = LocalDateTime.parse("2023-08-05T15:00:00.0"),
            timesToWaterDuringInterval = 3,
            waterAmount = 12.5,
            land = landForMocking,
            sensor = sensorForMocking
        )

        every { IIrrigationConfigurationRepository.findAll() } returns listOf(irrigationConfigurationExpected1, irrigationConfigurationExpected2)
        assertEquals(listOf(irrigationConfigurationExpected1, irrigationConfigurationExpected2).map { irrigationConfiguration -> irrigationConfigurationMapper.mapEntityToDtoResponse(irrigationConfiguration) }, irrigationConfigurationService.getAllIrrigationConfigurations())
    }

    @Test
    fun testGetIrrigationConfigurationById(){
        val irrigationConfigurationExpected = IrrigationConfiguration(
            id = 1,
            startDate = LocalDateTime.parse("2023-08-01T00:00:00.0"),
            endDate = LocalDateTime.parse("2023-08-05T15:00:00.0"),
            timesToWaterDuringInterval = 3,
            waterAmount = 12.5,
            land = landForMocking,
            sensor = sensorForMocking
        )

        every { IIrrigationConfigurationRepository.findById(1L) } returns Optional.of(irrigationConfigurationExpected)
        assertEquals(irrigationConfigurationMapper.mapEntityToDtoResponse(irrigationConfigurationExpected), irrigationConfigurationService.getIrrigationConfigurationById(1L))
    }

    @Test
    fun testGetIrrigationConfigurationByInvalidId(){
        every { IIrrigationConfigurationRepository.findById(2L) } returns Optional.empty()
        assertThrows<IdDoesNotExistException> { irrigationConfigurationService.getIrrigationConfigurationById(2L) }
    }

    @Test
    fun testDeleteIrrigationConfigurationById(){
        val irrigationConfigurationExpected = IrrigationConfiguration(
            id = 1,
            startDate = LocalDateTime.parse("2023-08-01T00:00:00.0"),
            endDate = LocalDateTime.parse("2023-08-05T15:00:00.0"),
            timesToWaterDuringInterval = 3,
            waterAmount = 12.5,
            land = landForMocking,
            sensor = sensorForMocking
        )

        every { IIrrigationConfigurationRepository.findById(1L) } returns Optional.of(irrigationConfigurationExpected)
        every { IIrrigationConfigurationRepository.deleteById(1L) } returns Unit
        assertEquals("Irrigation configuration with ID 1 has been deleted", irrigationConfigurationService.deleteIrrigationConfigurationById(1L))
    }

    @Test
    fun testDeleteIrrigationConfigurationByInvalidId(){
        every { IIrrigationConfigurationRepository.findById(2L) } returns Optional.empty()
        assertThrows<IdDoesNotExistException> { irrigationConfigurationService.deleteIrrigationConfigurationById(2L) }

    }
}