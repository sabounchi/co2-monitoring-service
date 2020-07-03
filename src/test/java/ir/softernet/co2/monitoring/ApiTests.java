package ir.softernet.co2.monitoring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import ir.softernet.co2.monitoring.controller.v1.SensorControllerV1;
import ir.softernet.co2.monitoring.dto.MeasurementsData;
import ir.softernet.co2.monitoring.dto.SensorMetrics;
import ir.softernet.co2.monitoring.dto.SensorStatus;
import ir.softernet.co2.monitoring.exception.base.ResultCode;
import ir.softernet.co2.monitoring.model.CO2Level;
import ir.softernet.co2.monitoring.service.ResponseService;
import ir.softernet.co2.monitoring.service.SensorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;

import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = SensorControllerV1.class)
public class ApiTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ResponseService responseService;

    @MockBean
    private SensorService sensorService;


    @Test
    public void shouldCollectReturn200HttpStatusCode() throws Exception {
        final String sensorUUID = "test";
        final MeasurementsData measurementsData = new MeasurementsData();
        measurementsData.setCo2(1700);
        measurementsData.setTime(new Date());

        doNothing().when(sensorService).collect(sensorUUID, measurementsData);
        doCallRealMethod().when(responseService).of(ResultCode.OK);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(measurementsData);

        mockMvc.perform(
                post("/api/v1/sensors/" + sensorUUID + "/measurements")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson)
        ).andExpect(status().is2xxSuccessful());
    }

    @Test
    public void shouldSensorStatusReturn200HttpStatusCode() throws Exception {
        final String sensorUUID = "test";

        when(sensorService.status(sensorUUID)).thenReturn(new SensorStatus(CO2Level.OK));
        doCallRealMethod().when(responseService).of(ResultCode.OK);

        mockMvc.perform(get("/api/v1/sensors/" + sensorUUID))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void shouldSensorMetricsReturn200HttpStatusCode() throws Exception {
        final String sensorUUID = "test";

        when(sensorService.metrics(sensorUUID)).thenReturn(new SensorMetrics());
        doCallRealMethod().when(responseService).of(ResultCode.OK);

        mockMvc.perform(get("/api/v1/sensors/" + sensorUUID + "/metrics"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void shouldAlertsReturn200HttpStatusCode() throws Exception {
        final String sensorUUID = "test";

        when(sensorService.alerts(sensorUUID)).thenReturn(new ArrayList<>());
        doCallRealMethod().when(responseService).of(ResultCode.OK);

        mockMvc.perform(get("/api/v1/sensors/" + sensorUUID + "/alerts"))
                .andExpect(status().is2xxSuccessful());
    }
}
