package nguyenthanhtung.datn.repository.auth;

import nguyenthanhtung.datn.dto.auth.request.RequestLoginDTO;
import nguyenthanhtung.datn.dto.auth.request.RequestRegisterDTO;
import nguyenthanhtung.datn.entity.auth.LoginEntity;
import nguyenthanhtung.datn.entity.base.RepositoryResult;

public interface AuthRepository {
    RepositoryResult register(RequestRegisterDTO requestRegisterDTO);
    LoginEntity login(RequestLoginDTO requestLoginDTO);
}
