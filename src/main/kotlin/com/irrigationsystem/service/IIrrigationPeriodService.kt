package com.irrigationsystem.service

import com.irrigationsystem.dto.IrrigationPeriodDtoResponse
import com.irrigationsystem.entity.IrrigationPeriod
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
interface IIrrigationPeriodService {

    fun createIrrigationPeriod(irrigationPeriod: IrrigationPeriod): IrrigationPeriod

    fun scheduledTaskForSensorOperation()
}