package com.irrigationsystem.service

import com.irrigationsystem.dto.IrrigationConfigurationDtoRequest
import com.irrigationsystem.dto.IrrigationConfigurationDtoResponse
import com.irrigationsystem.entity.IrrigationConfiguration
import com.irrigationsystem.entity.IrrigationPeriod
import com.irrigationsystem.entity.Land
import com.irrigationsystem.entity.Sensor
import com.irrigationsystem.exceptions.IdDoesNotExistException
import com.irrigationsystem.exceptions.InvalidRequestBodyException
import com.irrigationsystem.mapper.IrrigationConfigurationMapper
import com.irrigationsystem.repository.ILandRepository
import com.irrigationsystem.repository.ISensorRepository
import com.irrigationsystem.repository.IrrigationConfigurationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.time.LocalDateTime
import java.util.Optional
import kotlin.math.roundToLong

@Service
@Transactional
class IrrigationConfigurationService(
    @Autowired private val irrigationConfigurationRepository: IrrigationConfigurationRepository,
    @Autowired private val landRepository: ILandRepository,
    @Autowired private val sensorRepository: ISensorRepository,
    @Autowired private val irrigationPeriodService: IrrigationPeriodService
): IIrrigationConfigurationService {

    private val irrigationConfigurationMapper = IrrigationConfigurationMapper()

    private lateinit var savedIrrigationConfiguration: IrrigationConfiguration

    override fun createIrrigationConfiguration(irrigationConfigurationDtoRequest: IrrigationConfigurationDtoRequest): IrrigationConfigurationDtoResponse {
        if(irrigationConfigurationDtoRequest.sensorId == null || irrigationConfigurationDtoRequest.landId == null){
            throw InvalidRequestBodyException("Please add land ID and sensor ID")
        }
        if(irrigationConfigurationDtoRequest.startDate == null || irrigationConfigurationDtoRequest.endDate == null){
            throw InvalidRequestBodyException("Please add start and end date")
        }
        if(irrigationConfigurationDtoRequest.timesToWaterDuringInterval == null){
            throw InvalidRequestBodyException("Please add how many times the land should be watered during the interval")
        }
        if(irrigationConfigurationDtoRequest.waterAmount == null){
            throw InvalidRequestBodyException("Please add the water amount")
        }

        val optionalLand: Optional<Land> = landRepository.findById(irrigationConfigurationDtoRequest.landId!!)
        val optionalSensor: Optional<Sensor> = sensorRepository.findById(irrigationConfigurationDtoRequest.sensorId!!)
        if(optionalLand.isEmpty || optionalSensor.isEmpty){
            throw InvalidRequestBodyException("No Land or Sensor was found for the given ID's")
        }

        val irrigationConfiguration: IrrigationConfiguration = irrigationConfigurationMapper.mapDtoRequestToEntity(irrigationConfigurationDtoRequest)
        irrigationConfiguration.land = optionalLand.get()
        irrigationConfiguration.sensor = optionalSensor.get()

        savedIrrigationConfiguration = irrigationConfigurationRepository.save(irrigationConfiguration)
        createIrrigationConfigurationHelper(savedIrrigationConfiguration)
        return irrigationConfigurationMapper.mapEntityToDtoResponse(savedIrrigationConfiguration)
    }

    override fun getAllIrrigationConfigurations(): List<IrrigationConfigurationDtoResponse> {
        return irrigationConfigurationRepository.findAll().map { irrigationConfiguration -> irrigationConfigurationMapper.mapEntityToDtoResponse(irrigationConfiguration) }
    }

    override fun getIrrigationConfigurationById(irrigationConfigurationId: Long): IrrigationConfigurationDtoResponse {
        val optionalIrrigationConfiguration: Optional<IrrigationConfiguration> = irrigationConfigurationRepository.findById(irrigationConfigurationId)
        if(optionalIrrigationConfiguration.isEmpty){
            throw IdDoesNotExistException("No irrigation configuration with the given ID was found")
        }
        return irrigationConfigurationMapper.mapEntityToDtoResponse(optionalIrrigationConfiguration.get())
    }

    override fun deleteIrrigationConfigurationById(irrigationConfigurationId: Long): String {
        val optionalIrrigationConfiguration: Optional<IrrigationConfiguration> = irrigationConfigurationRepository.findById(irrigationConfigurationId)
        if(optionalIrrigationConfiguration.isEmpty){
            throw IdDoesNotExistException("No irrigation configuration with the given ID was found")
        }
        irrigationConfigurationRepository.deleteById(irrigationConfigurationId)
        return "Irrigation configuration with ID $irrigationConfigurationId has been deleted"
    }


    //this function is to create the irrigation period based on the start date, end date and times to water during the interval
    fun createIrrigationConfigurationHelper(irrigationConfiguration: IrrigationConfiguration){

        val minutes: Long = Duration.between(irrigationConfiguration.startDate, irrigationConfiguration.endDate).toMinutes()
        val periodOfWateringInMinutes: Long = (minutes / irrigationConfiguration.timesToWaterDuringInterval!! * 1.0).roundToLong()

        var startTime: LocalDateTime? = irrigationConfiguration.startDate

        for(i in 0 until irrigationConfiguration.timesToWaterDuringInterval!!){
            val irrigationPeriod = IrrigationPeriod(
                startTime = startTime,
                irrigationConfiguration = irrigationConfiguration
            )
            if (startTime != null) {
                startTime = startTime.plusMinutes(periodOfWateringInMinutes)
            }
            irrigationConfiguration.addIrrigationPeriod(irrigationPeriodService.createIrrigationPeriod(irrigationPeriod))
        }




    }

}