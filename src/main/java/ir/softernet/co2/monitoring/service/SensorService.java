package ir.softernet.co2.monitoring.service;

import ir.softernet.co2.monitoring.dto.*;
import ir.softernet.co2.monitoring.exception.ItemNotFound;
import ir.softernet.co2.monitoring.exception.base.AnException;
import ir.softernet.co2.monitoring.model.CO2Level;
import ir.softernet.co2.monitoring.model.MeasurementLog;
import ir.softernet.co2.monitoring.model.Status;
import ir.softernet.co2.monitoring.repository.MeasurementLogRepository;
import ir.softernet.co2.monitoring.repository.StatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class that implement business logics for API of {@link ir.softernet.co2.monitoring.controller.v1.SensorControllerV1}
 * uses RabbitMQ enqueue service: {@link ir.softernet.co2.monitoring.service.RabbitMQSenderService}
 *
 * also db repositories:
 * {@link ir.softernet.co2.monitoring.repository.MeasurementLogRepository}
 * {@link ir.softernet.co2.monitoring.repository.StatusRepository}
 *
 * @author saman
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SensorService {

    private final RabbitMQSenderService rabbitMQSenderService;

    private final MeasurementLogRepository measurementLogRepository;
    private final StatusRepository statusRepository;


    /**
     * collects sensor submitted data asynchronously(enqueue data for processing later)
     * @param uuid the uuid of sensor that submitted data
     * @param data data from sensor, contains CO2 amount and time of measurement
     * @throws AnException
     */
    public void collect(String uuid, MeasurementsData data) {
        rabbitMQSenderService.send(of(uuid, data));
    }

    /**
     * retrieves last state of specific sensor
     *
     * @param uuid the uuid of sensor
     * @return last state of specific sensor
     * @throws AnException
     */
    public SensorStatus status(String uuid) throws AnException {
        final Status status = statusRepository
                .findFirstByUuidOrderByIdDesc(uuid)
                .orElseThrow(ItemNotFound::new);

        return new SensorStatus(status.getLevel());
    }

    /**
     * calculates and query Maximum and Average CO2 amount of specific sensor
     *
     * @param uuid the uuid of sensor
     * @return Maximum and Average CO2 amount of specific sensor
     * @throws AnException
     */
    public SensorMetrics metrics(String uuid) {
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -30);//30 Days before

        final SensorMetrics metrics = new SensorMetrics();
        metrics.setMaxLast30Days(measurementLogRepository.maxCO2FromSpecificDateTillNow(uuid, calendar.getTime()));
        metrics.setAvgLast30Days(measurementLogRepository.avgCO2FromSpecificDateTillNow(uuid, calendar.getTime()));

        return metrics;
    }

    /**
     * retrieves all alert states of specific sensor with details such as an alert time interval and measurements log data
     *
     * @param uuid the uuid of sensor
     * @return all alert states of specific sensor with details such as an alert time interval and measurements log data
     * @throws AnException
     */
    @Transactional
    public List<AlertEntry> alerts(String uuid) {
        return statusRepository.findAllByUuidAndLevelOrderByIdDesc(uuid, CO2Level.ALERT)
                .filter(status -> status.getMeasurementLogs().size()==3)
                .map(item -> {
                    final AlertEntry entry = new AlertEntry();

                    final List<MeasurementLog> sorted = item.getMeasurementLogs().stream()
                            .sorted(Comparator.comparing(MeasurementLog::getMoment))
                            .collect(Collectors.toList());

                    entry.setStartTime(sorted.get(0).getMoment());
                    entry.setMeasurement1(sorted.get(0).getCo2());
                    entry.setMeasurement2(sorted.get(1).getCo2());
                    entry.setMeasurement3(sorted.get(2).getCo2());
                    entry.setEndTime(sorted.get(2).getMoment());

                    return entry;
                }).collect(Collectors.toList());
    }


    private SensorMeasurementsData of(String uuid, MeasurementsData data) {
        final SensorMeasurementsData sensorData = new SensorMeasurementsData();
        sensorData.setUuid(uuid);
        sensorData.setCo2(data.getCo2());
        sensorData.setTime(data.getTime());

        return sensorData;
    }

}
