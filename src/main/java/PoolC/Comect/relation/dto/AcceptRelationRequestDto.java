package PoolC.Comect.relation.dto;

import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class AcceptRelationRequestDto {
    private String email;
    private String friendNickname;
}
