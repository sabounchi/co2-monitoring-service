package ir.softernet.co2.monitoring.service;

import ir.softernet.co2.monitoring.dto.Response;
import ir.softernet.co2.monitoring.exception.base.AnException;
import ir.softernet.co2.monitoring.exception.base.ResultCode;
import org.springframework.stereotype.Service;

/**
 * Service class for translating ResultCodes {@link ir.softernet.co2.monitoring.exception.base.ResultCode} or,
 * predefined exceptions which extends {@link ir.softernet.co2.monitoring.exception.base.AnException} to
 * message-based json presentation of {@link ir.softernet.co2.monitoring.dto.Response}
 *
 * @author saman
 */
@Service
public class ResponseService {

  public Response of(ResultCode resultCode) {
    return new Response(resultCode.name());
  }

  public Response of(AnException e) {
    return of(e.getResultCode());
  }

}
