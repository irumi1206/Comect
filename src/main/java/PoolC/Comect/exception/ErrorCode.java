package PoolC.Comect.exception;

import co.elastic.clients.elasticsearch.nodes.Http;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.http.protocol.HTTP;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode{

    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "이메일이 존재하지 않음"),
    PATH_NOT_VALID(HttpStatus.NOT_FOUND,"경로가 유효하지 않음"),
    FILE_CONFLICT(HttpStatus.CONFLICT,"이미 같은 이름의 파일이 존재함"),
    INVALID_KEYWORD(HttpStatus.BAD_REQUEST,"키워드가 형식에맞지 않음"),

    //member
    COOKIE_ROTTEN(HttpStatus.UNAUTHORIZED,"쿠키가 유효하지 않음"),
    LOGIN_FAIL(HttpStatus.NOT_FOUND,"로그인 실패"),
    EMAIL_EXISTS(HttpStatus.FORBIDDEN,"이미 존재하는 이메일"),
    REQUEST_EXIST(HttpStatus.BAD_REQUEST,"이미 존재하는 요청"),
    REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND,"요청을 찾을 수 없습니다"),
    NICKNAME_EXISTS(HttpStatus.FORBIDDEN,"이미 존재하는 닉네임"),
    IMAGE_SAVE_CANCELED(HttpStatus.NOT_FOUND,"이미지 저장 실패"),
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND,"해당 아이디의 이미지 없음"),
    NOT_MY_IMAGE(HttpStatus.BAD_REQUEST,"내가 올린 이미지가 아님");




    private final HttpStatus httpStatus;
    private final String detail;

}
