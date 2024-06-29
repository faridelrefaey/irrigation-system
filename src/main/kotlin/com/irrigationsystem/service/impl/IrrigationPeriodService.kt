package com.irrigationsystem.service.impl

import com.irrigationsystem.dto.IrrigationPeriodDtoResponse
import com.irrigationsystem.entity.IrrigationPeriod
import com.irrigationsystem.mapper.IrrigationPeriodMapper
import com.irrigationsystem.repository.IIrrigationPeriodRepository
import com.irrigationsystem.service.IIrrigationPeriodService
import com.irrigationsystem.service.ISensorService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class IrrigationPeriodService(
    @Autowired private val irrigationPeriodRepository: IIrrigationPeriodRepository,
    @Autowired private val sensorService: ISensorService
) : IIrrigationPeriodService {

    private val irrigationPeriodMapper = IrrigationPeriodMapper()

    @Value("\${sensor.retry.attempts}")
    private lateinit var sensorRetryAttemptsString: String

    override fun createIrrigationPeriod(irrigationPeriod: IrrigationPeriod): IrrigationPeriod {
        return irrigationPeriodRepository.save(irrigationPeriod)
    }

    @Scheduled(fixedDelay = 30000)
    override fun scheduledTaskForSensorOperation() {
        val sensorRetryAttempts: Int = Integer.parseInt(sensorRetryAttemptsString)
        val irrigationPeriodsWithIsSuccessfulFalseList: MutableList<IrrigationPeriod> =
            irrigationPeriodRepository.getAllIrrigationPeriodWithIsSuccessfulFalse().toMutableList()

        //looping over all irrigation periods with is successful false
        for (irrigationPeriod in irrigationPeriodsWithIsSuccessfulFalseList) {
            //checking that the irrigation period start time has passed
            if (irrigationPeriod.startTime?.compareTo(LocalDateTime.now())!! < 1) {
                var operationDurationInMillis: Long =
                    sensorService.turnOnSensor(irrigationPeriod.irrigationConfiguration?.sensor?.id!!)

                // 0 operationDurationInMillis means that the sensor did not turn on
                if (operationDurationInMillis == 0L) {
                    //retry call attempts to sensor
                    for (i in 0 until sensorRetryAttempts) {
                        operationDurationInMillis =
                            sensorService.turnOnSensor(irrigationPeriod.irrigationConfiguration?.sensor?.id!!)
                        //if operationDurationInMillis is greater than zero, this means the sensor turned on
                        if (operationDurationInMillis > 0L) {
                            //updating the data for this irrigation period and persisting it to the database
                            irrigationPeriod.isSuccessful = true
                            irrigationPeriod.endTime = LocalDateTime.now()
                            irrigationPeriodRepository.save(irrigationPeriod)
                            break
                        }
                    }
                }
                //this means that the sensor turned on from the first call attempt
                else {
                    //updating the data for this irrigation period and persisting it to the database
                    irrigationPeriod.isSuccessful = true
                    irrigationPeriod.endTime = LocalDateTime.now()
                    irrigationPeriodRepository.save(irrigationPeriod)
                }
            }
        }
    }

    override fun getAllIrrigationPeriodsForIrrigationConfigurationId(irrigationConfigurationId: Long): List<IrrigationPeriodDtoResponse> {
        return irrigationPeriodRepository.getAllIrrigationPeriodsForIrrigationConfigurationId(irrigationConfigurationId)
            .map { irrigationPeriod -> irrigationPeriodMapper.mapEntityToDtoResponse(irrigationPeriod) }
    }


}