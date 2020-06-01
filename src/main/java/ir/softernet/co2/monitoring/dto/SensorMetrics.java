package ir.softernet.co2.monitoring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensorMetrics {

    private Integer maxLast30Days;
    private Integer avgLast30Days;

}
