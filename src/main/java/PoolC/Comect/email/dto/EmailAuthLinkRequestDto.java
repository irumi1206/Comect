package PoolC.Comect.email.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class EmailAuthLinkRequestDto {

    @NotNull
    private String id;
}
