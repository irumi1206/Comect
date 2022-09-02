package PoolC.Comect.user.dto;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import lombok.Data;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class CreateUserRequestDto {

    @NotNull
    @Email
    private String email;

    @Size(min=6,max=15)
    private String password;

    @NotNull
    @Size(max=10)
    @Pattern(regexp="^[a-zA-z가-힣 ]+$")
    private String nickname;

    @Parameter(
            description = "Files to be uploaded",
            content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE)  // Won't work without OCTET_STREAM as the mediaType.
    )
    private MultipartFile multipartFile;
}
