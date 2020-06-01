package ir.softernet.co2.monitoring.controller.v1;

import ir.softernet.co2.monitoring.dto.AlertEntry;
import ir.softernet.co2.monitoring.dto.MeasurementsData;
import ir.softernet.co2.monitoring.dto.SensorMetrics;
import ir.softernet.co2.monitoring.dto.SensorStatus;
import ir.softernet.co2.monitoring.exception.base.AnException;
import ir.softernet.co2.monitoring.service.SensorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * SensorController: main handler of http requests
 * processing the requests using {@link ir.softernet.co2.monitoring.service.SensorService}
 *
 * API docs available in /swagger-ui.html path
 *
 * @version 1
 * @author saman
 */
@RestController
@RequestMapping("/api/v1/sensors")
@RequiredArgsConstructor
@Slf4j
public class SensorControllerV1 {

    private final SensorService sensorService;


    @PostMapping("/{uuid}/measurements")
    public ResponseEntity<?> collect(@PathVariable("uuid") String uuid,
                                     @RequestBody @Valid MeasurementsData data) throws AnException {

        sensorService.collect(uuid, data);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<SensorStatus> sensorStatus(@PathVariable("uuid") String uuid) throws AnException {

        final SensorStatus data = sensorService.status(uuid);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/{uuid}/metrics")
    public ResponseEntity<SensorMetrics> sensorMetrics(@PathVariable("uuid") String uuid) throws AnException {

        final SensorMetrics data = sensorService.metrics(uuid);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/{uuid}/alerts")
    public ResponseEntity<List<AlertEntry>> alerts(@PathVariable("uuid") String uuid) throws AnException {

        final List<AlertEntry> data = sensorService.alerts(uuid);
        return ResponseEntity.ok(data);
    }
}
