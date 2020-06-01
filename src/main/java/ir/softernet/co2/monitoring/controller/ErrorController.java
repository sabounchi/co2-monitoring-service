package ir.softernet.co2.monitoring.controller;

import ir.softernet.co2.monitoring.aspect.SkipInvokeLogging;
import ir.softernet.co2.monitoring.exception.BadRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller that overrides default behaviour of Spring ErrorHandling,
 * by throwing {@link ir.softernet.co2.monitoring.exception.BadRequest},
 * which eventually translated to a massage-based json presentation of {@link ir.softernet.co2.monitoring.dto.Response},
 * instead of general /error spring page
 *
 * @author saman
 */
@RestController
@Slf4j
public class ErrorController
  implements org.springframework.boot.web.servlet.error.ErrorController {

  private static final String ERROR_PATH = "/error";
  @Override
  public String getErrorPath() {
    return ERROR_PATH;
  }


  @SkipInvokeLogging
  @GetMapping(ERROR_PATH)
  public ResponseEntity<Void> errorHandler() throws BadRequest {
    throw new BadRequest();
  }

}
