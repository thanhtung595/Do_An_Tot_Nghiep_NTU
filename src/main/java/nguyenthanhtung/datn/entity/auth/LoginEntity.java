package nguyenthanhtung.datn.entity.auth;

import lombok.Data;

@Data
public class LoginEntity {
    int id;
    String username;
    String passwordhash;
    String role;
}
