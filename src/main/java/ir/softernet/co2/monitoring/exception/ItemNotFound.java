package ir.softernet.co2.monitoring.exception;

import ir.softernet.co2.monitoring.exception.base.AnException;
import ir.softernet.co2.monitoring.exception.base.ResultCode;

public class ItemNotFound extends AnException {

  public ItemNotFound() {
    super(ResultCode.NOT_FOUND);
  }

}
