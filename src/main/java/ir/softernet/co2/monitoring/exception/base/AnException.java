package ir.softernet.co2.monitoring.exception.base;

import org.springframework.http.HttpStatus;

/**
 * base class of every predefined exceptions of application
 *
 * @author saman
 */
public class AnException extends Exception {

    private final ResultCode resultCode;


    public AnException(ResultCode resultCode) {
        super(resultCode.name());
        this.resultCode = resultCode;
    }

    public AnException(ResultCode resultCode, Throwable cause) {
        super(resultCode.name(), cause);
        this.resultCode = resultCode;
    }


    public ResultCode getResultCode() {
        return resultCode;
    }

    public HttpStatus getHttpStatus() {
        return resultCode.getHttpStatus();
    }

}
