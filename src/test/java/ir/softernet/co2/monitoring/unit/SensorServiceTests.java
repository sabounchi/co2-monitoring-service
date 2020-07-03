package ir.softernet.co2.monitoring.unit;

import ir.softernet.co2.monitoring.dto.MeasurementsData;
import ir.softernet.co2.monitoring.dto.SensorMeasurementsData;
import ir.softernet.co2.monitoring.dto.SensorMetrics;
import ir.softernet.co2.monitoring.exception.ItemNotFound;
import ir.softernet.co2.monitoring.exception.base.AnException;
import ir.softernet.co2.monitoring.model.CO2Level;
import ir.softernet.co2.monitoring.model.Status;
import ir.softernet.co2.monitoring.repository.MeasurementLogRepository;
import ir.softernet.co2.monitoring.repository.StatusRepository;
import ir.softernet.co2.monitoring.service.RabbitMQSenderService;
import ir.softernet.co2.monitoring.service.SensorService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SensorServiceTests {

    @Mock
    RabbitMQSenderService rabbitMQSenderService;

    @Mock
    MeasurementLogRepository measurementLogRepository;

    @Mock
    StatusRepository statusRepository;

    @InjectMocks
    SensorService sensorService;


    private String testUUID = "test";
    private int sampleCo2Level = 1750;
    private MeasurementsData sampleMeasurementsData;
    private Status OKStatus;
    private int avgCO2For30Days = 2000;
    private int maxCO2For30Days = 3200;
    private SensorMetrics testMetrics;

    @Before
    public void setup() {
        sampleMeasurementsData = new MeasurementsData();
        sampleMeasurementsData.setCo2(sampleCo2Level);
        sampleMeasurementsData.setTime(new Date());

        OKStatus = new Status();
        OKStatus.setUuid(testUUID);
        OKStatus.setLevel(CO2Level.OK);

        testMetrics = new SensorMetrics(maxCO2For30Days, avgCO2For30Days);
    }


    @Test
    public void testCollect() {
        final SensorMeasurementsData sensorData = new SensorMeasurementsData();
        sensorData.setUuid(testUUID);
        sensorData.setCo2(sampleMeasurementsData.getCo2());
        sensorData.setTime(sampleMeasurementsData.getTime());

        doNothing().when(rabbitMQSenderService).send(sensorData);
        assertDoesNotThrow(() -> sensorService.collect(testUUID, sampleMeasurementsData));
    }

    @Test
    public void testStatusThrowsItemNotFound() {
        assertThrows(ItemNotFound.class, () -> sensorService.status(UUID.randomUUID().toString()));
    }

    @Test
    public void testStatusReturnOK() throws AnException {
        when(statusRepository.findFirstByUuidOrderByIdDesc(testUUID)).thenReturn(Optional.of(OKStatus));

        assertEquals(CO2Level.OK, sensorService.status(testUUID).getStatus());
    }

    @Test
    public void testMetrics() {
        when(measurementLogRepository.avgCO2FromSpecificDateTillNow(eq(testUUID), any(Date.class))).thenReturn(avgCO2For30Days);
        when(measurementLogRepository.maxCO2FromSpecificDateTillNow(eq(testUUID), any(Date.class))).thenReturn(maxCO2For30Days);

        assertEquals(testMetrics.getAvgLast30Days(), sensorService.metrics(testUUID).getAvgLast30Days(), "Incorrect Average");
        assertEquals(testMetrics.getMaxLast30Days(), sensorService.metrics(testUUID).getMaxLast30Days(), "Incorrect Maximum");
    }

    @Test
    public void testAlerts() {
        when(statusRepository.findAllByUuidAndLevelOrderByIdDesc(testUUID, CO2Level.ALERT)).thenReturn(Stream.empty());

        assertEquals(0, sensorService.alerts(testUUID).size());
    }

}
