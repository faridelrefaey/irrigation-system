package com.irrigationsystem.controller

import com.irrigationsystem.dto.IrrigationConfigurationDtoRequest
import com.irrigationsystem.dto.IrrigationConfigurationDtoResponse
import com.irrigationsystem.entity.IrrigationConfiguration
import com.irrigationsystem.security.annotations.IsEngineerUser
import com.irrigationsystem.security.annotations.IsFarmerUser
import com.irrigationsystem.service.IIrrigationConfigurationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/irr")
class IrrigationConfigurationController(@Autowired private val iIrrigationConfigurationService: IIrrigationConfigurationService) {

    @PostMapping
    @IsEngineerUser
    fun createIrrigationConfiguration(@RequestBody irrigationConfigurationDtoRequest: IrrigationConfigurationDtoRequest): ResponseEntity<IrrigationConfigurationDtoResponse>{
        return ResponseEntity(iIrrigationConfigurationService.createIrrigationConfiguration(irrigationConfigurationDtoRequest), HttpStatus.CREATED)
    }

    @GetMapping //can be used by both roles
    fun getAllIrrigationConfiguration(): ResponseEntity<List<IrrigationConfigurationDtoResponse>>{
        return ResponseEntity(iIrrigationConfigurationService.getAllIrrigationConfigurations(), HttpStatus.OK)
    }

    @GetMapping("{id}") //can be used by both roles
    fun getIrrigationConfigurationById(@PathVariable("id") irrigationConfigurationId: Long): ResponseEntity<IrrigationConfigurationDtoResponse>{
        return ResponseEntity(iIrrigationConfigurationService.getIrrigationConfigurationById(irrigationConfigurationId), HttpStatus.OK)
    }

    @DeleteMapping("{id}")
    @IsEngineerUser
    fun deleteIrrigationConfigurationById(@PathVariable("id") irrigationConfigurationId: Long): ResponseEntity<String>{
        return ResponseEntity(iIrrigationConfigurationService.deleteIrrigationConfigurationById(irrigationConfigurationId), HttpStatus.OK)
    }

}