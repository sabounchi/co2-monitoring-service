package ir.softernet.co2.monitoring.exception;

import ir.softernet.co2.monitoring.exception.base.AnException;
import ir.softernet.co2.monitoring.exception.base.ResultCode;

public class NotImplemented extends AnException {

  public NotImplemented() {
    super(ResultCode.NOT_IMPLEMENTED);
  }

}
