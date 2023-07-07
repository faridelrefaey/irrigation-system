package com.irrigationsystem.service

import com.irrigationsystem.dto.IrrigationConfigurationDtoRequest
import com.irrigationsystem.dto.IrrigationConfigurationDtoResponse
import com.irrigationsystem.entity.IrrigationConfiguration
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
interface IIrrigationConfigurationService {

    fun createIrrigationConfiguration(irrigationConfigurationDtoRequest: IrrigationConfigurationDtoRequest): IrrigationConfigurationDtoResponse

    fun getAllIrrigationConfigurations(): List<IrrigationConfigurationDtoResponse>

    fun getIrrigationConfigurationById(irrigationConfigurationId: Long): IrrigationConfigurationDtoResponse

    fun deleteIrrigationConfigurationById(irrigationConfigurationId: Long): String
}