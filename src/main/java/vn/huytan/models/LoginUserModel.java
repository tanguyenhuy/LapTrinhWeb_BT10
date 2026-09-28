package vn.huytan.models;
import lombok.*;
@Data @NoArgsConstructor @AllArgsConstructor
public class LoginUserModel {
    private String email;
    private String password;
}