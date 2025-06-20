package nguyenthanhtung.datn.repository.users;

import nguyenthanhtung.datn.dto.users.request.EditPasswordRequestDTO;
import nguyenthanhtung.datn.dto.users.request.UpdateRequestDTO;
import nguyenthanhtung.datn.entity.base.RepositoryResult;

import java.util.List;
import java.util.Map;

public interface UsersRepository {
    Map<String, Object> getProfile();
    List<Map<String, Object>> getAllUser();
    void updateUser(UpdateRequestDTO updateRequestDTO);
    RepositoryResult editPassword(EditPasswordRequestDTO editPasswordRequestDTO);
    void uploadImage(Integer id , String fileName);
}
