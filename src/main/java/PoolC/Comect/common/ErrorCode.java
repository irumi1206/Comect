package PoolC.Comect.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "이메일이 존재하지 않음"),
    EMAIL_NOT_VALID(HttpStatus.BAD_REQUEST,"이메일 형식이 잘못됨"),
    PATH_NOT_VALID(HttpStatus.NOT_FOUND,"경로가 유효하지 않음");


    private final HttpStatus httpStatus;
    private final String detail;

}
