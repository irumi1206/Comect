package PoolC.Comect.user.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UpdateUserRequestDto {
    @Email
    private String email;

    @NotNull
    @Size(max=10)
    @Pattern(regexp="^[a-zA-z가-힣 ]+$")
    private String newNickname;

    private Boolean imageChange;

    private MultipartFile newMultipartFile;
}
