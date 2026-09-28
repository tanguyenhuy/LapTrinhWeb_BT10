package vn.huytan.models;
import lombok.*;
@Data @NoArgsConstructor @AllArgsConstructor
public class RegisterUserModel {
    private String email;
    private String password;
    private String fullName;
}