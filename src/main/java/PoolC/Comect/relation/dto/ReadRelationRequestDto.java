package PoolC.Comect.relation.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class ReadRelationRequestDto {

    private String email;
}
