package nguyenthanhtung.datn.dto.users.request;

import lombok.Data;

@Data
public class EditPasswordRequestDTO {
    int id;
    String currentPassword;
    String newPassword;
}
