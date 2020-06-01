package ir.softernet.co2.monitoring.exception.base;

import org.springframework.http.HttpStatus;

public enum ResultCode {

  OK(HttpStatus.OK),
  BAD_REQUEST(HttpStatus.BAD_REQUEST),
  FIELD_VALIDATION_PROBLEM(HttpStatus.BAD_REQUEST),
  NOT_FOUND(HttpStatus.NOT_FOUND),
  NOT_IMPLEMENTED(HttpStatus.NOT_IMPLEMENTED),
  ERROR(HttpStatus.INTERNAL_SERVER_ERROR)
  ;

  private final HttpStatus httpStatus;

  ResultCode(HttpStatus httpStatus) {
    this.httpStatus = httpStatus;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

}
