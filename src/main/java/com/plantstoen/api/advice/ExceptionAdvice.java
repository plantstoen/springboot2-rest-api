package com.plantstoen.api.advice;

import com.plantstoen.api.advice.exception.CEmailSignInFailedException;
import com.plantstoen.api.advice.exception.CUserNotFoundException;
import com.plantstoen.api.model.response.CommonResult;
import com.plantstoen.api.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;


@RequiredArgsConstructor
@RestControllerAdvice // 별도의 속성값 없이 사용하면 모든 컨트롤러 담당하게됨
public class ExceptionAdvice {

    private final ResponseService responseService;

    private final MessageSource messageSource;

    // Exception.class는 최상위 예외처리 객체이므로 다른 ExceptionHandler에서 걸러지지 않은 예외가 있으면 최정적으로 이 handler를 거쳐 처리
    @ExceptionHandler(Exception.class) // @Controller, @RestController 가 적용된 Bean내에서 발생하는 예외를 잡아줌
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult defaultException(HttpServletRequest request, Exception e) {
        return responseService.getFailResult(Integer.parseInt(getMessage("unKnown.code")), getMessage("unKnown.msg"));
    }

    @ExceptionHandler(CUserNotFoundException.class) // Controller에서 CUserNotFoundException이 발생하면 이 Handler에서 받아서 처리
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult userNotFoundException(HttpServletRequest request, CUserNotFoundException e) {
        return responseService.getFailResult(Integer.parseInt(getMessage("userNotFound.code")), getMessage("userNotFound.msg"));
    }

    @ExceptionHandler(CEmailSignInFailedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult emailSignInFailed(HttpServletRequest request, CEmailSignInFailedException e) {
        return responseService.getFailResult(Integer.parseInt(getMessage("emailSignInFailed.code")), getMessage("emailSignInFailed.msg"));
    }

    // code 정보에 해당하는 메시지 조회
    private String getMessage(String code) {
        return getMessage(code, null);
    }

    // code정보, 추가 argument로 현재 locale에 맞는 메세지 조회
    private String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
