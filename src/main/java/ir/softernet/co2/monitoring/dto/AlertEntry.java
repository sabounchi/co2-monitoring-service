package ir.softernet.co2.monitoring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlertEntry {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date startTime;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date endTime;

    private Integer  measurement1;

    private Integer  measurement2;

    private Integer  measurement3;

}
