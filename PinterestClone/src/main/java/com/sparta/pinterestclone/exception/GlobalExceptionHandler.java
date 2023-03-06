package com.sparta.pinterestclone.exception;

import com.sparta.pinterestclone.utils.dto.MessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<MessageDto> methodValidException(MethodArgumentNotValidException e) {
        MessageDto responseDto = makeErrorResponse(e.getBindingResult());
        return ResponseEntity.badRequest().body(responseDto);
    }

    private MessageDto makeErrorResponse(BindingResult bindingResult) {
        String message = "";
        if (bindingResult.hasErrors()) {
            message = bindingResult.getAllErrors().get(0).getDefaultMessage();
        }
        return MessageDto.of(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ApiException.class})
    protected ResponseEntity<ErrorResponse> handleCustomException(ApiException e) {
        log.error("handleDataException throw Exception : {}", e.getException());
        return ErrorResponse.toResponseEntity(e.getException());
    }
}
