package com.irrigationsystem

import com.fasterxml.jackson.databind.ObjectMapper
import com.irrigationsystem.dto.SensorDtoRequest
import com.irrigationsystem.service.ISensorService
import com.irrigationsystem.service.SensorService
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.AfterEach
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

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class SensorControllerTest(
    @Autowired private val sensorService: ISensorService,
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val mapper: ObjectMapper
    ) {

    private lateinit var sensorDtoRequest1: SensorDtoRequest
    private lateinit var sensorDtoRequest2: SensorDtoRequest

    @BeforeEach
    fun setUp(){
        sensorDtoRequest1 = SensorDtoRequest(sensorName = "Sensor1")
        sensorDtoRequest2 = SensorDtoRequest(sensorName = "Sensor2")
    }

    @Test
    fun testCreateSensor(){
        mockMvc.perform(MockMvcRequestBuilders.post("/sensor")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(sensorDtoRequest1)))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.sensorName").value("Sensor1"))
    }

    @Test
    fun testGetAllSensors(){
        sensorService.createSensor(sensorDtoRequest1)
        sensorService.createSensor(sensorDtoRequest2)

        mockMvc.perform(MockMvcRequestBuilders.get("/sensor"))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$", hasSize<Any>(2)))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].sensorName").value("Sensor1"))
            .andExpect(jsonPath("$[1].id").value(2))
            .andExpect(jsonPath("$[1].sensorName").value("Sensor2"))
    }

    @Test
    fun testGetSensorById(){
        sensorService.createSensor(sensorDtoRequest1)

        mockMvc.perform(MockMvcRequestBuilders.get("/sensor/{id}", 1))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.sensorName").value("Sensor1"))
    }

    @Test
    fun testEditSensorById(){
        sensorService.createSensor(sensorDtoRequest1)

        mockMvc.perform(MockMvcRequestBuilders.put("/sensor/{id}", 1)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(sensorDtoRequest2)))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.sensorName").value("Sensor2"))
    }

}