package com.irrigationsystem

import com.fasterxml.jackson.databind.ObjectMapper
import com.irrigationsystem.dto.IrrigationConfigurationDtoRequest
import com.irrigationsystem.dto.LandDtoRequest
import com.irrigationsystem.dto.SensorDtoRequest
import com.irrigationsystem.entity.IrrigationConfiguration
import com.irrigationsystem.service.IrrigationConfigurationService
import com.irrigationsystem.service.LandService
import com.irrigationsystem.service.SensorService
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDateTime

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class IrrigationConfigurationControllerTest(
    @Autowired private val irrigationConfigurationService: IrrigationConfigurationService,
    @Autowired private val landService: LandService,
    @Autowired private val sensorService: SensorService,
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val objectMapper: ObjectMapper
) {

    private lateinit var irrigationConfigurationDtoRequest1: IrrigationConfigurationDtoRequest
    private lateinit var landDtoRequestToBeUsed: LandDtoRequest
    private lateinit var sensorDtoRequestToBeUsed: SensorDtoRequest
    private lateinit var irrigationConfigurationDtoRequest2: IrrigationConfigurationDtoRequest

    @BeforeEach
    fun setUp(){
        irrigationConfigurationDtoRequest1 = IrrigationConfigurationDtoRequest(
            startDate = LocalDateTime.parse("2023-08-01T00:00:00.0"),
            endDate = LocalDateTime.parse("2023-08-01T15:00:00.0"),
            timesToWaterDuringInterval = 2,
            waterAmount = 12.5,
            landId = 1,
            sensorId = 1
        )
        irrigationConfigurationDtoRequest2 = IrrigationConfigurationDtoRequest(
            startDate = LocalDateTime.parse("2023-08-01T00:00:00.0"),
            endDate = LocalDateTime.parse("2023-08-01T15:00:00.0"),
            timesToWaterDuringInterval = 2,
            waterAmount = 12.5,
            landId = 2,
            sensorId = 2
        )

        landDtoRequestToBeUsed = LandDtoRequest(seedType = "Corn", landName = "Corn Field", area = 12.5)
        sensorDtoRequestToBeUsed = SensorDtoRequest(sensorName = "Sensor1")

    }

    @Test
    fun testCreateIrrigationConfiguration(){
        landService.createLand(landDtoRequestToBeUsed)
        sensorService.createSensor(sensorDtoRequestToBeUsed)
        mockMvc.perform(MockMvcRequestBuilders.post("/irr")
            .content(objectMapper.writeValueAsString(irrigationConfigurationDtoRequest1))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.startDate").value("2023-08-01T00:00:00"))
            .andExpect(jsonPath("$.endDate").value("2023-08-01T15:00:00"))
            .andExpect(jsonPath("$.timesToWaterDuringInterval").value(2))
            .andExpect(jsonPath("$.waterAmount").value(12.5))
            .andExpect(jsonPath("$.sensor.id").value(1))
            .andExpect(jsonPath("$.sensor.sensorName").value("Sensor1"))
            .andExpect(jsonPath("$.irrigationPeriodList", hasSize<Any>(2)))
            .andExpect(jsonPath("$.irrigationPeriodList[0].id").value(1))
            .andExpect(jsonPath("$.irrigationPeriodList[0].startTime").value("2023-08-01T00:00:00"))
            .andExpect(jsonPath("$.irrigationPeriodList[0].endTime").value(null))
            .andExpect(jsonPath("$.irrigationPeriodList[0].isSuccessful").value(false))
            .andExpect(jsonPath("$.irrigationPeriodList[1].id").value(2))
            .andExpect(jsonPath("$.irrigationPeriodList[1].startTime").value("2023-08-01T07:30:00"))
            .andExpect(jsonPath("$.irrigationPeriodList[1].endTime").value(null))
            .andExpect(jsonPath("$.irrigationPeriodList[1].isSuccessful").value(false))
    }

    @Test
    fun testGetAllIrrigationConfiguration(){
        landService.createLand(landDtoRequestToBeUsed)
        sensorService.createSensor(sensorDtoRequestToBeUsed)
        landService.createLand(landDtoRequestToBeUsed)
        sensorService.createSensor(sensorDtoRequestToBeUsed)

        irrigationConfigurationService.createIrrigationConfiguration(irrigationConfigurationDtoRequest1)
        irrigationConfigurationService.createIrrigationConfiguration(irrigationConfigurationDtoRequest2)


        mockMvc.perform(MockMvcRequestBuilders.get("/irr"))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$", hasSize<Any>(2)))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[1].id").value(2))
    }

    @Test
    fun testGetIrrigationConfigurationById(){
        landService.createLand(landDtoRequestToBeUsed)
        sensorService.createSensor(sensorDtoRequestToBeUsed)
        landService.createLand(landDtoRequestToBeUsed)
        sensorService.createSensor(sensorDtoRequestToBeUsed)

        irrigationConfigurationService.createIrrigationConfiguration(irrigationConfigurationDtoRequest1)
        irrigationConfigurationService.createIrrigationConfiguration(irrigationConfigurationDtoRequest2)

        mockMvc.perform(MockMvcRequestBuilders.get("/irr/{id}", 1))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1))
    }

    @Test
    fun testDeleteIrrigationConfigurationById(){
        landService.createLand(landDtoRequestToBeUsed)
        sensorService.createSensor(sensorDtoRequestToBeUsed)

        irrigationConfigurationService.createIrrigationConfiguration(irrigationConfigurationDtoRequest1)

        mockMvc.perform(MockMvcRequestBuilders.delete("/irr/{id}", 1))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").value("Irrigation configuration with ID 1 has been deleted"))
    }

}