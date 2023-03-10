package com.springstudy.studypractice.exception.handler.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springstudy.studypractice.exception.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        List<String> messages = new ArrayList<>();
        messages.add("접근 권한이 없습니다.");

        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .errorType(HttpStatus.UNAUTHORIZED.name())
                .messages(messages)
                .path(request.getRequestURI())
                .build();

        response.setStatus(errorResponseDto.getStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());

        response.getWriter().write(mapper.writeValueAsString(errorResponseDto));
    }
}
