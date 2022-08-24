package PoolC.Comect.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 유저 정보를 찾을 수 없습니다"),
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND,"이메일을 찾을수 없습니다"),
    EMAIL_NOT_VALID(HttpStatus.BAD_REQUEST,"이메일 형식이 잘못됨"),
    PATH_NOT_VALID(HttpStatus.NOT_FOUND,"경로가 유효하지 않음");


    private final HttpStatus httpStatus;
    private final String detail;

}
