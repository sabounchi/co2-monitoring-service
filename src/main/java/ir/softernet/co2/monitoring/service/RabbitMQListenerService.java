package ir.softernet.co2.monitoring.service;

import ir.softernet.co2.monitoring.dto.SensorMeasurementsData;
import ir.softernet.co2.monitoring.model.MeasurementLog;
import ir.softernet.co2.monitoring.model.Status;
import ir.softernet.co2.monitoring.repository.MeasurementLogRepository;
import ir.softernet.co2.monitoring.repository.StatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * Service that consume from queued sensors submitted data and do these operations:
 * 1- decide for status using: {@link ir.softernet.co2.monitoring.service.StatusService}
 * 2- persists the sensor measurement log
 * 3- change and persists last state of specific sensor
 *
 * @author saman
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMQListenerService {

    private final StatusService statusService;

    private final MeasurementLogRepository measurementLogRepository;
    private final StatusRepository statusRepository;


    @Transactional
    @RabbitListener(queues = "${rabbitmq.queue}")
    public void receiveMessage(SensorMeasurementsData data) {
        log.info("got data form queue: " + data);
        try {

            final Collection<MeasurementLog> sequence = measurementLogRepository
                    .findFirst2ByUuidAndMomentBeforeOrderByMomentDesc(data.getUuid(), data.getTime());


            final MeasurementLog aLog = of(data);
            measurementLogRepository.save(aLog);


            final Status status = statusService.decideStatus(aLog, sequence);
            if (status != null)
                statusRepository.save(status);


        } catch(Exception e) {
            log.warn("exception on consume message: " + data, e);
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }


    private MeasurementLog of(SensorMeasurementsData data) {
        final MeasurementLog aLog = new MeasurementLog();
        aLog.setUuid(data.getUuid());
        aLog.setCo2(data.getCo2());
        aLog.setMoment(data.getTime());

        return aLog;
    }

}
