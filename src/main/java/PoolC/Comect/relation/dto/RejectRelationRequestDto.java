package PoolC.Comect.relation.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class RejectRelationRequestDto {

    private String email;
    private String friendNickname;

    public static Object builder() {
        return null;
    }
}
