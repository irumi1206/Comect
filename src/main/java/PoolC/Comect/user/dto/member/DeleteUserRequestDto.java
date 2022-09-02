package PoolC.Comect.user.dto;

import lombok.Data;

@Data
public class DeleteUserRequestDto {
    private String email;
    private String password;
}
