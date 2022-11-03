package PoolC.Comect.email;

import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection="email")
@Getter
@Setter
@ToString
public class PasswordChangeEmail{

    @Id
    @Generated
    private ObjectId id;
    private String email;
    private LocalDateTime validDate;
    private int randomNumber;

    public PasswordChangeEmail(String email) {
        this.id=new ObjectId();
        this.email = email;
        this.validDate= LocalDateTime.now().plusHours(24);
        this.randomNumber=(int) ((Math.random()*(9)+1)*100000);
    }
    public boolean isValid(){
        return LocalDateTime.now().isBefore(validDate);
    }
}
