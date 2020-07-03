package ir.softernet.co2.monitoring.service;

import ir.softernet.co2.monitoring.model.CO2Level;
import ir.softernet.co2.monitoring.model.MeasurementLog;
import ir.softernet.co2.monitoring.model.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Service that decide status from received log
 *
 * @author saman
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class StatusService {

    private static final int THRESHOLD = 2000;


    public Status decideStatus(MeasurementLog aLog, Collection<MeasurementLog> sequence) {

        final boolean previousWasOk = sequence.stream().allMatch(item -> item.getCo2() < THRESHOLD);
        final boolean previousWasNotOk = sequence.stream().allMatch(item -> item.getCo2() > THRESHOLD);

        if (aLog.getCo2() > THRESHOLD) {//WARN or ALERT

            final Status status = new Status();
            status.setUuid(aLog.getUuid());
            if (sequence.size()==2 && previousWasNotOk) {//ALERT

                status.setLevel(CO2Level.ALERT);

                status.addMeasurementLog(aLog);
                status.addAllMeasurementLog(sequence);

            } else {//WARN

                status.setLevel(CO2Level.WARN);

                status.addMeasurementLog(aLog);
            }

            return status;

        } else {//maybe OK

            if (sequence.size()<2 || previousWasOk) {//OK

                final Status status = new Status();

                status.setUuid(aLog.getUuid());
                status.setLevel(CO2Level.OK);

                status.addMeasurementLog(aLog);
                status.addAllMeasurementLog(sequence);

                return status;
            }

        }

        return null;
    }

}
