package com.irrigationsystem.service.impl

import com.irrigationsystem.dto.IrrigationConfigurationDtoRequest
import com.irrigationsystem.dto.IrrigationConfigurationDtoResponse
import com.irrigationsystem.dto.IrrigationPeriodDtoRequest
import com.irrigationsystem.dto.IrrigationPeriodDtoResponse
import com.irrigationsystem.entity.IrrigationConfiguration
import com.irrigationsystem.entity.IrrigationPeriod
import com.irrigationsystem.entity.Land
import com.irrigationsystem.entity.Sensor
import com.irrigationsystem.exceptions.IrrigationSystemException
import com.irrigationsystem.mapper.IrrigationConfigurationMapper
import com.irrigationsystem.mapper.IrrigationPeriodMapper
import com.irrigationsystem.repository.ILandRepository
import com.irrigationsystem.repository.ISensorRepository
import com.irrigationsystem.repository.IIrrigationConfigurationRepository
import com.irrigationsystem.security.repository.UserRepository
import com.irrigationsystem.security.utils.JwtUtils
import com.irrigationsystem.service.IIrrigationConfigurationService
import com.irrigationsystem.service.IIrrigationPeriodService
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
    @Autowired private val IIrrigationConfigurationRepository: IIrrigationConfigurationRepository,
    @Autowired private val landRepository: ILandRepository,
    @Autowired private val sensorRepository: ISensorRepository,
    @Autowired private val irrigationPeriodService: IIrrigationPeriodService,
    @Autowired private val userRepository: UserRepository,
) : IIrrigationConfigurationService {

    private val irrigationConfigurationMapper = IrrigationConfigurationMapper()
    private val irrigationPeriodMapper = IrrigationPeriodMapper()


    override fun createIrrigationConfiguration(irrigationConfigurationDtoRequest: IrrigationConfigurationDtoRequest): IrrigationConfigurationDtoResponse {
        createIrrigationConfigurationValidation(irrigationConfigurationDtoRequest)

        val optionalLand: Optional<Land> = landRepository.findById(irrigationConfigurationDtoRequest.landId!!)
        val optionalSensor: Optional<Sensor> = sensorRepository.findById(irrigationConfigurationDtoRequest.sensorId!!)
        if (optionalLand.isEmpty || optionalSensor.isEmpty) {
            throw IrrigationSystemException("No Land or Sensor was found for the given ID's")
        }
        checkIrrigationConfigurationsSensors(
            irrigationConfigurationDtoRequest.sensorId!!,
            irrigationConfigurationDtoRequest.landId!!
        )
        checkIrrigationConfigurationsSensorsAndLandsCombinations(
            irrigationConfigurationDtoRequest.sensorId!!,
            irrigationConfigurationDtoRequest.landId!!,
            irrigationConfigurationDtoRequest.startDate!!,
            irrigationConfigurationDtoRequest.endDate!!
        )

        val username = JwtUtils.getUsernameFromToken()
        val user = userRepository.findByUsername(username)
        val irrigationConfigurationToBeSaved: IrrigationConfiguration =
            irrigationConfigurationMapper.mapDtoRequestToEntity(irrigationConfigurationDtoRequest, user!!)
        irrigationConfigurationToBeSaved.land = optionalLand.get()
        irrigationConfigurationToBeSaved.sensor = optionalSensor.get()

        val savedIrrigationConfiguration: IrrigationConfiguration =
            IIrrigationConfigurationRepository.save(createIrrigationConfigurationHelper(irrigationConfigurationToBeSaved))

        val searchId: Long? = savedIrrigationConfiguration.id

        val irrigationPeriodDtoResponseForIrrigationConfiguration: List<IrrigationPeriodDtoResponse> =
            irrigationPeriodService.getAllIrrigationPeriodsForIrrigationConfigurationId(searchId!!)

        val irrigationConfigurationDtoResponseToBeReturned: IrrigationConfigurationDtoResponse =
            irrigationConfigurationMapper.mapEntityToDtoResponse(savedIrrigationConfiguration)

        irrigationConfigurationDtoResponseToBeReturned.irrigationPeriodList =
            irrigationPeriodDtoResponseForIrrigationConfiguration.toMutableList()
        return irrigationConfigurationDtoResponseToBeReturned
    }

    private fun createIrrigationConfigurationValidation(irrigationConfigurationDtoRequest: IrrigationConfigurationDtoRequest) {
        if (irrigationConfigurationDtoRequest.sensorId == null || irrigationConfigurationDtoRequest.landId == null) {
            throw IrrigationSystemException("Please add land ID and sensor ID")
        }
        if (irrigationConfigurationDtoRequest.startDate == null || irrigationConfigurationDtoRequest.endDate == null) {
            throw IrrigationSystemException("Please add start and end date")
        }
        if (irrigationConfigurationDtoRequest.timesToWaterDuringInterval == null) {
            throw IrrigationSystemException("Please add how many times the land should be watered during the interval")
        }
        if (irrigationConfigurationDtoRequest.waterAmount == null) {
            throw IrrigationSystemException("Please add the water amount")
        }
        if (irrigationConfigurationDtoRequest.startDate!! > irrigationConfigurationDtoRequest.endDate) {
            throw IrrigationSystemException("Start date cannot be after end date")
        }
        if (irrigationConfigurationDtoRequest.startDate!! < LocalDateTime.now()) {
            throw IrrigationSystemException("Start date cannot be in the past.")
        }
    }

    // this function is to check if any sensor has any running configurations
    private fun checkIrrigationConfigurationsSensors(
        sensorId: Long,
        landId: Long
    ) {
        val irrigationConfigurationList: List<IrrigationConfiguration> =
            IIrrigationConfigurationRepository.getAllIrrigationConfigurationsForSensorIdAndNotForLandId(sensorId, landId)
        for (irrigationConfiguration: IrrigationConfiguration in irrigationConfigurationList) {
            for (irrigationPeriod: IrrigationPeriod in irrigationConfiguration.irrigationPeriodList) {
                if (!irrigationPeriod.isSuccessful) {
                    throw IrrigationSystemException("Sensor could not be assigned to an another configuration since it still has a running configuration.")
                }
            }
        }
    }

    //this function checks if the sensor has a running configuration, then we can create an another configuration if it has the same land and the date ranges do not intersect
    private fun checkIrrigationConfigurationsSensorsAndLandsCombinations(
        sensorId: Long,
        landId: Long,
        startDateToBeChecked: LocalDateTime,
        endDateToBeChecked: LocalDateTime
    ) {
        val irrigationConfigurationList: List<IrrigationConfiguration> =
            IIrrigationConfigurationRepository.getAllIrrigationConfigurationForSensorIdAndLandId(sensorId, landId)
        for (irrigationConfiguration: IrrigationConfiguration in irrigationConfigurationList) {
            if (!(endDateToBeChecked < irrigationConfiguration.startDate || startDateToBeChecked > irrigationConfiguration.endDate)) {
                throw IrrigationSystemException("A configuration with the chosen date range cannot be created as it intersects with an already existing configuration")
            }
        }
    }

    //this function is to create the irrigation period based on the start date, end date and times to water during the interval
    private fun createIrrigationConfigurationHelper(irrigationConfiguration: IrrigationConfiguration): IrrigationConfiguration {
        val minutes: Long =
            Duration.between(irrigationConfiguration.startDate, irrigationConfiguration.endDate).toMinutes()
        val periodOfWateringInMinutes: Long =
            (minutes / irrigationConfiguration.timesToWaterDuringInterval!! * 1.0).roundToLong()

        var startTime: LocalDateTime = irrigationConfiguration.startDate!!

        for (i in 0 until irrigationConfiguration.timesToWaterDuringInterval!!) {
            val irrigationPeriodDtoRequest = IrrigationPeriodDtoRequest(
                startTime = startTime
            )
            startTime = startTime.plusMinutes(periodOfWateringInMinutes)
            val mappedIrrigationPeriod: IrrigationPeriod =
                irrigationPeriodMapper.mapDtoRequestToEntity(irrigationPeriodDtoRequest)
            mappedIrrigationPeriod.irrigationConfiguration = irrigationConfiguration
            irrigationPeriodService.createIrrigationPeriod(mappedIrrigationPeriod)
        }
        return irrigationConfiguration
    }

    override fun getAllIrrigationConfigurations(): List<IrrigationConfigurationDtoResponse> {
        return IIrrigationConfigurationRepository.findAll().map { irrigationConfiguration ->
            irrigationConfigurationMapper.mapEntityToDtoResponse(irrigationConfiguration)
        }
    }

    override fun getIrrigationConfigurationById(irrigationConfigurationId: Long): IrrigationConfigurationDtoResponse {
        val optionalIrrigationConfiguration: Optional<IrrigationConfiguration> =
            IIrrigationConfigurationRepository.findById(irrigationConfigurationId)
        if (optionalIrrigationConfiguration.isEmpty) {
            throw IrrigationSystemException("No irrigation configuration with the given ID was found")
        }
        return irrigationConfigurationMapper.mapEntityToDtoResponse(optionalIrrigationConfiguration.get())
    }

    override fun deleteIrrigationConfigurationById(irrigationConfigurationId: Long): String {
        val optionalIrrigationConfiguration: Optional<IrrigationConfiguration> =
            IIrrigationConfigurationRepository.findById(irrigationConfigurationId)
        if (optionalIrrigationConfiguration.isEmpty) {
            throw IrrigationSystemException("No irrigation configuration with the given ID was found")
        }
        IIrrigationConfigurationRepository.deleteById(irrigationConfigurationId)
        return "Irrigation configuration with ID $irrigationConfigurationId has been deleted"
    }
}