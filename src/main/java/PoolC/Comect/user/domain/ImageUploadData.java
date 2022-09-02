package PoolC.Comect.user.domain;

import PoolC.Comect.image.domain.Image;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class ImageUploadData {
    private boolean success = false;
    private ObjectId imageId = null;

}
