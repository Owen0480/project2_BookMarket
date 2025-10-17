package com.springboot.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
@Getter
public class CategoryException extends RuntimeException {
    private String errorMessage;
    private String category;

    public CategoryException(String category) {
        this.category = category;
        this.errorMessage = String.format("요청한 도서 분야 '%s'를 찾을 수 없습니다.", category);
    }

    public CategoryException() {
        this.errorMessage = "요청한 도서 분야를 찾을 수 없습니다.";
    }
}
