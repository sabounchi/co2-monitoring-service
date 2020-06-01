package ir.softernet.co2.monitoring.controller;

import ir.softernet.co2.monitoring.dto.Response;
import ir.softernet.co2.monitoring.exception.FieldValidationProblem;
import ir.softernet.co2.monitoring.exception.base.AnException;
import ir.softernet.co2.monitoring.exception.base.ResultCode;
import ir.softernet.co2.monitoring.service.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Controller advice that translate possible exceptions (which extends application base exception {@link ir.softernet.co2.monitoring.exception.base.AnException})
 * into a massage-based json presentation of {@link ir.softernet.co2.monitoring.dto.Response},
 * by {@link ir.softernet.co2.monitoring.service.ResponseService}
 *
 * @author saman
 */
@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ErrorControllerAdvice {

    private final ResponseService responseService;

    private ResponseEntity<Response> buildResponseEntity(AnException e) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(responseService.of(e));
    }

    /**
     * handles every predefined exceptions
     *
     * @param e
     * @return json response woth proper http status code
     */
    @ExceptionHandler(AnException.class)
    public ResponseEntity<Response> baseExceptionHandler(AnException e) {
        return buildResponseEntity(e);
    }

    /**
     * handles input validation problem
     *
     * @param e
     * @return json response woth proper http status code
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> processValidationError(MethodArgumentNotValidException e) {
        return buildResponseEntity(new FieldValidationProblem());
    }

    /**
     * handles every unknown exceptions
     *
     * @param e
     * @return json response woth proper http status code
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> unknownExceptionHandler(Exception e) {
        return buildResponseEntity(new AnException(ResultCode.ERROR, e));
    }

}
