package com.irrigationsystem.controller

import com.irrigationsystem.dto.LandDtoRequest
import com.irrigationsystem.dto.LandDtoResponse
import com.irrigationsystem.service.ILandService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/land")
class LandController(@Autowired private val iLandService: ILandService) {

    @PostMapping
    fun createLand(@RequestBody landDtoRequest: LandDtoRequest): ResponseEntity<LandDtoResponse>{
        return ResponseEntity(iLandService.createLand(landDtoRequest), HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    fun editLand(@PathVariable("id") landId: Long, @RequestBody landDtoRequest: LandDtoRequest?): ResponseEntity<LandDtoResponse>{
        return ResponseEntity(iLandService.editLand(landId, landDtoRequest), HttpStatus.OK)
    }

    @GetMapping
    fun getAllLands(): ResponseEntity<List<LandDtoResponse>>{
        return ResponseEntity(iLandService.getAllLands(), HttpStatus.OK)
    }

    @GetMapping("{id}")
    fun getLandById(@PathVariable("id") landId: Long): ResponseEntity<LandDtoResponse>{
        return ResponseEntity(iLandService.getLandById(landId), HttpStatus.OK)
    }

    @DeleteMapping("{id}")
    fun deleteLandById(@PathVariable("id") landId: Long): ResponseEntity<String>{
        return ResponseEntity(iLandService.deleteLandById(landId), HttpStatus.OK)
    }

}