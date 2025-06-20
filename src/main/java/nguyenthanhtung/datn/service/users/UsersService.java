package nguyenthanhtung.datn.service.users;

import nguyenthanhtung.datn.dto.base.BaseResponse;
import nguyenthanhtung.datn.dto.users.request.EditPasswordRequestDTO;
import nguyenthanhtung.datn.dto.users.request.UpdateRequestDTO;
import org.springframework.web.multipart.MultipartFile;

public interface UsersService {
    BaseResponse getProfile();
    BaseResponse getAllUser();
    BaseResponse updateUser(UpdateRequestDTO updateRequestDTO);
    BaseResponse editPassword(EditPasswordRequestDTO editPasswordRequestDTO);
    BaseResponse uploadImage(MultipartFile file);
}
