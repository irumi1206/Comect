package PoolC.Comect.folder.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FolderCheckPathResponseDto {

    private int valid;

    @Builder
    public FolderCheckPathResponseDto(int valid){
        this.valid=valid;
    }
}
