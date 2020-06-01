package ir.softernet.co2.monitoring.dto;

import ir.softernet.co2.monitoring.model.CO2Level;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SensorStatus {

    private CO2Level status;

}
