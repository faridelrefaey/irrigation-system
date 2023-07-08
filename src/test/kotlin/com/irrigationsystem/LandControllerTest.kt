package com.irrigationsystem

import com.fasterxml.jackson.databind.ObjectMapper
import com.irrigationsystem.dto.LandDtoRequest
import com.irrigationsystem.repository.ILandRepository
import com.irrigationsystem.service.ILandService
import com.irrigationsystem.service.LandService
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
import org.springframework.test.web.servlet.RequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class LandControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val landService: ILandService,
    @Autowired private val objectMapper: ObjectMapper,
    @Autowired private val landRepository: ILandRepository
) {

    private lateinit var landDtoRequestToBeUsed1: LandDtoRequest
    private lateinit var landDtoRequestToBeUsed2: LandDtoRequest

    @BeforeEach
    fun setUp(){
        landDtoRequestToBeUsed1 = LandDtoRequest(seedType = "Corn", landName = "Corn Field", area = 12.5)
        landDtoRequestToBeUsed2 = LandDtoRequest(seedType = "Wheat", landName = "Wheat Field", area = 25.0)
    }


    @Test
    fun testCreateLand(){
        mockMvc.perform(MockMvcRequestBuilders.post("/land")
            .content(objectMapper.writeValueAsString(landDtoRequestToBeUsed1))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.seedType").value("Corn"))
            .andExpect(jsonPath("$.landName").value("Corn Field"))
            .andExpect(jsonPath("$.area").value(12.5))
            .andExpect(jsonPath("$.irrigationConfigurationList", hasSize<Any>(0)))
    }

    @Test
    fun testEditLandById(){
        landService.createLand(landDtoRequestToBeUsed2)
        mockMvc.perform(MockMvcRequestBuilders.put("/land/{id}", 1)
            .content(objectMapper.writeValueAsString(landDtoRequestToBeUsed1))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.seedType").value("Corn"))
            .andExpect(jsonPath("$.landName").value("Corn Field"))
            .andExpect(jsonPath("$.area").value(12.5))
            .andExpect(jsonPath("$.irrigationConfigurationList", hasSize<Any>(0)))
    }

    @Test
    fun testGetAllLands(){
        landService.createLand(landDtoRequestToBeUsed1)
        landService.createLand(landDtoRequestToBeUsed2)

        mockMvc.perform(MockMvcRequestBuilders.get("/land"))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$", hasSize<Any>(2)))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].seedType").value("Corn"))
            .andExpect(jsonPath("$[0].landName").value("Corn Field"))
            .andExpect(jsonPath("$[0].area").value(12.5))
            .andExpect(jsonPath("$[0].irrigationConfigurationList", hasSize<Any>(0)))
            .andExpect(jsonPath("$[1].id").value(2))
            .andExpect(jsonPath("$[1].seedType").value("Wheat"))
            .andExpect(jsonPath("$[1].landName").value("Wheat Field"))
            .andExpect(jsonPath("$[1].area").value(25.0))
            .andExpect(jsonPath("$[1].irrigationConfigurationList", hasSize<Any>(0)))
    }

    @Test
    fun testGetLandById(){
        landService.createLand(landDtoRequestToBeUsed1)
        landService.createLand(landDtoRequestToBeUsed2)

        mockMvc.perform(MockMvcRequestBuilders.get("/land/{id}", 1))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.seedType").value("Corn"))
            .andExpect(jsonPath("$.landName").value("Corn Field"))
            .andExpect(jsonPath("$.area").value(12.5))
            .andExpect(jsonPath("$.irrigationConfigurationList", hasSize<Any>(0)))
    }

    @Test
    fun testDeleteLandById(){
        landService.createLand(landDtoRequestToBeUsed1)

        mockMvc.perform(MockMvcRequestBuilders.delete("/land/{id}", 1))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").value("Land with ID 1 deleted successfully."))
    }
}