package PoolC.Comect.image.dto;

import lombok.Data;

@Data
public class CreateImageResponseDto {
    private String imageUrl;

    public CreateImageResponseDto(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
