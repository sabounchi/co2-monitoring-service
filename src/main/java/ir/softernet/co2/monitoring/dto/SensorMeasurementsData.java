package ir.softernet.co2.monitoring.dto;

import lombok.Data;

@Data
public class SensorMeasurementsData extends MeasurementsData {

    private String uuid;


    @Override
    public String toString() {
        return "SensorMeasurementsData{" +
                "uuid='" + uuid + '\'' +
                "} " + super.toString();
    }
}
