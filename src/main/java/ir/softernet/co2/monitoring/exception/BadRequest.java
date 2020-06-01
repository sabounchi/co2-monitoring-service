package ir.softernet.co2.monitoring.exception;

import ir.softernet.co2.monitoring.exception.base.AnException;
import ir.softernet.co2.monitoring.exception.base.ResultCode;

public class BadRequest extends AnException {

  public BadRequest() {
    super(ResultCode.BAD_REQUEST);
  }

}
