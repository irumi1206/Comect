package PoolC.Comect.user.domain;

import PoolC.Comect.image.domain.Image;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Document(collection="user")
@Getter
@Setter
@ToString
public class User implements UserDetails {
//public class User{

    @Id
    @Generated
    private ObjectId id;

    private String nickname;
    private String email;
    private ObjectId rootFolderId;
    private String imageUrl="";
    private List<ObjectId> followings;
    private List<ObjectId> followers;
    private String password;
    private List<String> roles = new ArrayList<>();

    public User(String nickname, String email, String password) {
        this.id=new ObjectId();
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.followings=new ArrayList<>();
        this.followers=new ArrayList<>();
        this.roles= Collections.singletonList("ROLE_UN");
    }
    public ObjectId getImageId(){
        if(this.imageUrl.equals("")){
            return null;
        }
        String[] split = imageUrl.split("id=");
        String s = split[split.length - 1];
        return new ObjectId(s);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return nickname;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
