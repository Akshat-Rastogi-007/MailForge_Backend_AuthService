package com.rastogi.mailforge.AuthService.error;

import com.rastogi.mailforge.AuthService.dto.response.ApiResponseDto;
import com.rastogi.mailforge.AuthService.error.errors.ResourceAlreadyExistsException;
import com.rastogi.mailforge.AuthService.error.errors.ResourceNotFoundException;
import com.rastogi.mailforge.AuthService.error.errors.UserAlreadyVerifiedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDto<?>> methodArgumentNotValidException(MethodArgumentNotValidException exception){

        Map<String,String> errors = new HashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage())
                );


        return new ResponseEntity<>(
                new ApiResponseDto<>("Bad Request", errors,HttpStatus.BAD_REQUEST)
                , HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseDto<?>> resourceNotFoundException(ResourceNotFoundException e){

        return new ResponseEntity<>(
                new ApiResponseDto<>(
                        "Resource not found", e.getMessage(),HttpStatus.BAD_REQUEST
                )
                ,
                HttpStatus.BAD_REQUEST
        );

    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ApiResponseDto<?>> resourceAlreadyExist(ResourceAlreadyExistsException e){

        return new ResponseEntity<>(
                new ApiResponseDto<>(
                        "Resource already exists", e.getMessage(),HttpStatus.BAD_REQUEST
                )
                ,
                HttpStatus.BAD_REQUEST
        );

    }

    @ExceptionHandler(UserAlreadyVerifiedException.class)
    public ResponseEntity<ApiResponseDto<?>> userAlreadyVerifiedException(UserAlreadyVerifiedException e){

        return new ResponseEntity<>(
                new ApiResponseDto<>(
                        "User is already verified", e.getMessage(),HttpStatus.BAD_REQUEST
                )
                ,
                HttpStatus.BAD_REQUEST
        );

    }


}
