package ir.softernet.co2.monitoring.unit;

import ir.softernet.co2.monitoring.model.CO2Level;
import ir.softernet.co2.monitoring.model.MeasurementLog;
import ir.softernet.co2.monitoring.service.StatusService;
import org.junit.Before;
import org.junit.Test;;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class StatusServiceTests {

    @InjectMocks
    StatusService statusService;


    private MeasurementLog anOKLog;
    private MeasurementLog aWARNLog;

    @Before
    public void setup() {
        anOKLog = new MeasurementLog();
        anOKLog.setMoment(new Date());
        anOKLog.setUuid("test");
        anOKLog.setCo2(1500);

        aWARNLog = new MeasurementLog();
        aWARNLog.setMoment(new Date());
        aWARNLog.setUuid("test");
        aWARNLog.setCo2(2500);
    }


    @Test
    public void testOKStatus() {
        assertEquals(CO2Level.OK, statusService.decideStatus(anOKLog, new ArrayList<>()).getLevel());
    }

    @Test
    public void testWARNStatus() {
        assertEquals(CO2Level.WARN, statusService.decideStatus(aWARNLog, new ArrayList<>()).getLevel());
    }

    @Test
    public void testALERTStatus() {
        final ArrayList<MeasurementLog> someWARN = new ArrayList<>();
        someWARN.add(aWARNLog);
        someWARN.add(aWARNLog);

        assertEquals(CO2Level.ALERT, statusService.decideStatus(aWARNLog, someWARN).getLevel());

    }

}
