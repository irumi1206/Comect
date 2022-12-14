package PoolC.Comect.folder.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class LinkDeleteRequestDto {

    private String path;
    private List<String> ids;

    @Builder
    public LinkDeleteRequestDto(String path,List<String> ids){
        this.path=path;
        this.ids=ids;
    }
}
