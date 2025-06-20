package nguyenthanhtung.datn.repository.users.implement;

import lombok.RequiredArgsConstructor;
import nguyenthanhtung.datn.config.requestThreadLocal.RequestThreadLocalContext;
import nguyenthanhtung.datn.config.requestThreadLocal.RequestUserThreadLocal;
import nguyenthanhtung.datn.constants.ConstantQuery.users.ConstantQueryUsers;
import nguyenthanhtung.datn.constants.ContantApplication;
import nguyenthanhtung.datn.dto.users.request.EditPasswordRequestDTO;
import nguyenthanhtung.datn.dto.users.request.UpdateRequestDTO;
import nguyenthanhtung.datn.entity.base.RepositoryResult;
import nguyenthanhtung.datn.repository.users.UsersRepository;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class ImplUsersRepository implements UsersRepository {

    private final JdbcTemplate  jdbcTemplate;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Map<String, Object> getProfile() {
        RequestUserThreadLocal currentUser = RequestThreadLocalContext.get();
        List<Map<String, Object>> data = Collections.emptyList();
        if (Objects.equals(currentUser.getRole(), ContantApplication.RolePatient)){
            data = jdbcTemplate.query(ConstantQueryUsers.SELECT_USER_BY_ID, new ColumnMapRowMapper(), currentUser.getUserId());
            if (data.getFirst().containsKey("image")) {
                data.getFirst().compute("image", (k, oldValue) -> System.getProperty("URL_AWS_STORAGE") + System.getProperty("PATH_AVATAR_PATIENTS") + oldValue);
            }
        }else if (Objects.equals(currentUser.getRole(), ContantApplication.RoleDoctor)){
            data = jdbcTemplate.query(ConstantQueryUsers.SELECT_USER_DOCTOR_BY_ID, new ColumnMapRowMapper(), currentUser.getUserId());
            if (data.getFirst().containsKey("image")) {
                data.getFirst().compute("image", (k, oldValue) -> System.getProperty("URL_AWS_STORAGE") + System.getProperty("PATH_AVATAR_DOCTOR") + oldValue);
            }
        }

        return data.getFirst();
    }

    @Override
    public List<Map<String, Object>> getAllUser() {
        return jdbcTemplate.query(ConstantQueryUsers.SELECT_ALL_USER, new ColumnMapRowMapper());
    }

    @Override
    public void updateUser(UpdateRequestDTO updateRequestDTO) {
        jdbcTemplate.update(ConstantQueryUsers.UPDATE_USER_BY_ID,
                updateRequestDTO.getFullname(),
                updateRequestDTO.getGender(),
                updateRequestDTO.getPhonenumber(),
                updateRequestDTO.getAddress(),
                updateRequestDTO.getId());
    }

    @Override
    public RepositoryResult editPassword(EditPasswordRequestDTO editPasswordRequestDTO) {
        RepositoryResult result = new RepositoryResult();

        // Lấy user theo id (không có điều kiện password)
        List<Map<String, Object>> userById = jdbcTemplate.query(ConstantQueryUsers.GET_USER_BY_ID, new ColumnMapRowMapper(),
                editPasswordRequestDTO.getId());
        if (userById.isEmpty()){
            result.setStatusCode(ContantApplication.StatusSuccess_No_Data);
            result.setMessage("Tài khoản không tồn tại.");
            return result;
        }

        Map<String, Object> user = userById.getFirst();
        String pass = (String) user.get("passwordhash");

        // So sánh password hiện tại với hash trong DB
        if (!passwordEncoder.matches(editPasswordRequestDTO.getCurrentPassword(), pass )) {
            result.setStatusCode(ContantApplication.StatusSuccess_No_Data);
            result.setMessage("Mật khẩu hiện tại không chính xác.");
            return result;
        }

        // Cập nhật mật khẩu mới
        String passNew = passwordEncoder.encode(editPasswordRequestDTO.getNewPassword());

        jdbcTemplate.update(ConstantQueryUsers.UPDATE_PASS, passNew, editPasswordRequestDTO.getId());
        result.setStatusCode(ContantApplication.StatusSuccess);
        result.setMessage("Đổi mật khẩu thành công");

        return result;
    }

    @Override
    public void uploadImage(Integer id, String fileName) {
        jdbcTemplate.update(ConstantQueryUsers.UPDATE_IMAGE, fileName, id);
    }
}
