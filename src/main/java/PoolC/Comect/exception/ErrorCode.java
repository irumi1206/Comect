package PoolC.Comect.exception;

import co.elastic.clients.elasticsearch.nodes.Http;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.http.protocol.HTTP;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode{

    //형식 400
    PATH_NOT_VALID(HttpStatus.BAD_REQUEST,"경로가 유효하지 않음"),
    INVALID_KEYWORD(HttpStatus.BAD_REQUEST,"키워드가 형식에맞지 않음"),

    //이미 존재하는 객체 409
    FILE_CONFLICT(HttpStatus.CONFLICT,"이미 같은 이름의 파일이 존재함"),
    EMAIL_EXISTS(HttpStatus.CONFLICT,"이미 존재하는 이메일"),
    FOLLOWING_EXIST(HttpStatus.CONFLICT,"이미 존재하는 팔로잉"),
    NICKNAME_EXISTS(HttpStatus.CONFLICT,"이미 존재하는 닉네임"),

    //객체가 존재하지 않음 404
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "이메일의 유저가 존재하지 않음"),
    REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND,"친구요청이 존재하지 않음"),
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND,"해당 아이디의 이미지 없음"),
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND,"해당 아이디의 이미지가 존재하지않습니다."),

    //그 외
    COOKIE_ROTTEN(HttpStatus.UNAUTHORIZED,"쿠키가 유효하지 않음"),
    LOGIN_FAIL(HttpStatus.NOT_FOUND,"로그인 실패"),
    IMAGE_SAVE_CANCELED(HttpStatus.NOT_FOUND,"이미지 저장 실패"),
    NOT_MY_IMAGE(HttpStatus.BAD_REQUEST,"내가 올린 이미지가 아님");




    private final HttpStatus httpStatus;
    private final String detail;

}
