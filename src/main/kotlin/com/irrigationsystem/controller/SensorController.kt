package com.irrigationsystem.controller

import com.irrigationsystem.dto.SensorDtoRequest
import com.irrigationsystem.dto.SensorDtoResponse
import com.irrigationsystem.service.ISensorService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/sensor")
class SensorController(@Autowired private val sensorService: ISensorService) {

    @PostMapping
    fun createSensor(@RequestBody sensorDtoRequest: SensorDtoRequest): ResponseEntity<SensorDtoResponse>{
        return ResponseEntity(sensorService.createSensor(sensorDtoRequest), HttpStatus.CREATED)
    }

    @GetMapping
    fun getAllSensors(): ResponseEntity<List<SensorDtoResponse>>{
        return ResponseEntity(sensorService.getAllSensors(), HttpStatus.OK)
    }

    @GetMapping("{id}")
    fun getSensorById(@PathVariable("id") sensorId: Long): ResponseEntity<SensorDtoResponse>{
        return ResponseEntity(sensorService.getSensorById(sensorId), HttpStatus.OK)
    }

    @PutMapping("{id}")
    fun editSensorById(@PathVariable("id") sensorId: Long, @RequestBody sensorDtoRequest: SensorDtoRequest?): ResponseEntity<SensorDtoResponse>{
        return ResponseEntity(sensorService.editSensorById(sensorId, sensorDtoRequest), HttpStatus.OK)
    }
}