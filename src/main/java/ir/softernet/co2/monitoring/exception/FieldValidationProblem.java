package ir.softernet.co2.monitoring.exception;

import ir.softernet.co2.monitoring.exception.base.AnException;
import ir.softernet.co2.monitoring.exception.base.ResultCode;

public class FieldValidationProblem extends AnException {

    public FieldValidationProblem() {
        super(ResultCode.FIELD_VALIDATION_PROBLEM);
    }

}
