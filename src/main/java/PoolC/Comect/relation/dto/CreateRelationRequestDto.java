package PoolC.Comect.relation.dto;

import lombok.Data;
import org.bson.types.ObjectId;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class CreateRelationRequestDto {

    private String email;
    private String friendNickname;
}
