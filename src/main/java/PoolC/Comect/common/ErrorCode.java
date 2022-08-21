package PoolC.Comect.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 유저 정보를 찾을 수 없습니다");

    private final HttpStatus httpStatus;
    private final String detail;

}
