package nguyenthanhtung.datn.service.auth;

import nguyenthanhtung.datn.dto.auth.request.RequestLoginDTO;
import nguyenthanhtung.datn.dto.auth.request.RequestRegisterDTO;
import nguyenthanhtung.datn.dto.base.BaseResponse;

public interface AuthService {
    BaseResponse register(RequestRegisterDTO requestRegisterDTO);
    BaseResponse login(RequestLoginDTO requestLoginDTO);

}
