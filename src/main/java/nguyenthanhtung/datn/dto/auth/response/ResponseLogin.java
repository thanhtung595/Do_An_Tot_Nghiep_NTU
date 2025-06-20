package nguyenthanhtung.datn.dto.auth.response;

import lombok.Data;

@Data
public class ResponseLogin {
    String accessToken;
    String refeshToken;
}
