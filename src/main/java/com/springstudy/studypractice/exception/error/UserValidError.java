package com.springstudy.studypractice.exception.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserValidError {

    DUPLICATE_USERNAME(HttpStatus.CONFLICT, "중복된 username입니다. 다른 username을 사용하세요."),
    INVALID_USERNAME_PASSWORD(HttpStatus.UNAUTHORIZED, "아아디 또는 비밀번호가 일치하지 않습니다."),
    USERNAME_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 username의 회원이 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
