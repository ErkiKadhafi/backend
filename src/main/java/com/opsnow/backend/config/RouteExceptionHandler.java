package com.opsnow.backend.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.opsnow.backend.utils.ApiException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@AllArgsConstructor
class ErrorObj {
    public int status;
    public String title;
    public String detail;
    public String instance;
}

@AllArgsConstructor
class InputErrorCustom {
    public String field;
    public String message;
}

@AllArgsConstructor
class ErrorObjInput {
    public int status;
    public String title;
    public String detail;
    public String instance;
    public Object data;
}

@ControllerAdvice
public class RouteExceptionHandler {
    @ExceptionHandler(value = { MethodArgumentNotValidException.class })
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        List<InputErrorCustom> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            InputErrorCustom errObj = new InputErrorCustom(fieldName, message);
            errors.add(errObj);
        });

        return new ResponseEntity<>(
                new ErrorObjInput(HttpStatus.BAD_REQUEST.value(), "Bad Request",
                        "Value of some fields doesn't match the requirements.",
                        request.getRequestURI().toString(), errors),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = { ApiException.class })
    public ResponseEntity<Object> handleApiException(ApiException ex, HttpServletRequest request) {
        return new ResponseEntity<>(
                new ErrorObj(ex.getStatus().value(), ex.getStatus().getReasonPhrase(), ex.getMessage(),
                        request.getRequestURI().toString()),
                ex.getStatus());
    }
}
