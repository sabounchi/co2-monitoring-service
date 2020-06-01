package ir.softernet.co2.monitoring.service;

import ir.softernet.co2.monitoring.dto.SensorMeasurementsData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service hepler class for enqueue sensor submitted measurement log data
 *
 * @author saman
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMQSenderService {

    private final AmqpTemplate rabbitTemplate;

    /**
     * Exchange name of application queue system
     */
    @Value("${rabbitmq.exchange}")
    private String exchange;

    /**
     * Routhing key of application queue system
     */
    @Value("${rabbitmq.routingkey}")
    private String routingkey;


    public void send(SensorMeasurementsData data) {
        rabbitTemplate.convertAndSend(exchange, routingkey, data);
        log.info("sent data to queue: " + data);
    }

}
