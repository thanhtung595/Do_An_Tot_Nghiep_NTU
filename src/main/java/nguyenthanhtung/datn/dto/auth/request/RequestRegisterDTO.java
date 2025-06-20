package nguyenthanhtung.datn.dto.auth.request;

import lombok.Data;
import nguyenthanhtung.datn.config.annotation.Trimmed;

@Data
public class RequestRegisterDTO {
    @Trimmed
    private String username;

    @Trimmed
    private String email;

    @Trimmed
    private String fullname;

    @Trimmed
    private String phone;

    @Trimmed
    private String password;
}
