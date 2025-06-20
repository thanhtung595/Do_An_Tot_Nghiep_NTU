package nguyenthanhtung.datn.dto.auth.request;

import lombok.Data;
import nguyenthanhtung.datn.config.annotation.Trimmed;

@Data
public class RequestLoginDTO {
    @Trimmed
    private String username;

    @Trimmed
    private String password;
}
