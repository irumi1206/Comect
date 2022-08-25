package PoolC.Comect.relation.dto;

import lombok.Data;

@Data
public class RejectRelationRequestDto {
    private String email;
    private String friendNickname;

    public static Object builder() {
        return null;
    }
}
