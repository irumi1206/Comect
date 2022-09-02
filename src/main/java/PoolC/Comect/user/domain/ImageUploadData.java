package PoolC.Comect.user.domain;

import PoolC.Comect.image.domain.Image;
import lombok.Data;

@Data
public class ImageUploadData {
    private boolean success = false;
    private Image image;

}
