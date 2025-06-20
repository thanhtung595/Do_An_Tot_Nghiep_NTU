package nguyenthanhtung.datn.repository.auth.implement;

import lombok.RequiredArgsConstructor;
import nguyenthanhtung.datn.constants.ConstantQuery.auth.ConstantQueryAuth;
import nguyenthanhtung.datn.constants.ContantApplication;
import nguyenthanhtung.datn.dto.auth.request.RequestLoginDTO;
import nguyenthanhtung.datn.dto.auth.request.RequestRegisterDTO;
import nguyenthanhtung.datn.entity.auth.LoginEntity;
import nguyenthanhtung.datn.entity.base.RepositoryResult;
import nguyenthanhtung.datn.repository.auth.AuthRepository;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ImplAuthRepository implements AuthRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;

    @Override
    public RepositoryResult register(RequestRegisterDTO requestRegisterDTO) {
        RepositoryResult result = new RepositoryResult();

        // Tao tham so check user
        Map<String, Object> paramsCheckUsers = new HashMap<>();
        paramsCheckUsers.put("username", requestRegisterDTO.getUsername());
        paramsCheckUsers.put("email", requestRegisterDTO.getEmail());
        paramsCheckUsers.put("phonenumber", requestRegisterDTO.getPhone());

        // Truy van danh sach cac field ton tai
        List<String> existingFields = namedParameterJdbcTemplate.query(
                ConstantQueryAuth.CHECK_USER_REGISTER_EXIT,
                paramsCheckUsers,
                (rs, rowNum) -> rs.getString("field")
        );

        // Kiem tra ton tai dua tung field
        if (existingFields.contains("username")) {
            result.setStatusCode(ContantApplication.StatusWarning_Conflict);
            result.setMessage("Tài khoản đã tồn tại");
            return result;
        }

        if (existingFields.contains("email")) {
            result.setStatusCode(ContantApplication.StatusWarning_Conflict);
            result.setMessage("Email đã tồn tại");
            return result;
        }

        if (existingFields.contains("phonenumber")) {
            result.setStatusCode(ContantApplication.StatusWarning_Conflict);
            result.setMessage("Số điện thoại đã tồn tại");
            return result;
        }

        // Tao tham so create user
        Map<String, Object> paramsCreateUsers = new HashMap<>();
        paramsCreateUsers.put("username", requestRegisterDTO.getUsername());
        paramsCreateUsers.put("passwordhash", passwordEncoder.encode(requestRegisterDTO.getPassword()));
        paramsCreateUsers.put("fullname", requestRegisterDTO.getFullname());
        paramsCreateUsers.put("phonenumber", requestRegisterDTO.getPhone());
        paramsCreateUsers.put("email", requestRegisterDTO.getEmail());
        paramsCreateUsers.put("address", "");
        paramsCreateUsers.put("image", "default.jpg");
        paramsCreateUsers.put("gender", "Khác");
        paramsCreateUsers.put("roleid", 3);

        // Create user
        Integer idUser = namedParameterJdbcTemplate.queryForObject(
                ConstantQueryAuth.CREATE_USER,
                new MapSqlParameterSource(paramsCreateUsers),
                Integer.class
        );

        if (idUser == null || idUser == 0) {
            result.setStatusCode(ContantApplication.StatusWarning_Conflict);
            result.setMessage("Bị lỗi do iduser bị 0 hoặc null");

            return result;
        }

        result.setStatusCode(ContantApplication.StatusSuccess);
        result.setMessage(ContantApplication.MessageSuccess);
        return result;
    }

    @Override
    public LoginEntity login(RequestLoginDTO requestLoginDTO) {

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("username", requestLoginDTO.getUsername());

        List<LoginEntity> loginEntities = namedParameterJdbcTemplate.query(
                ConstantQueryAuth.LOGIN_BY_USERNAME,
                params,
                DataClassRowMapper.newInstance(LoginEntity.class)
        );

        if (loginEntities.isEmpty()) {
            return null;
        }

        LoginEntity loginEntity = loginEntities.getFirst();

        if (!passwordEncoder.matches(requestLoginDTO.getPassword(), loginEntity.getPasswordhash())) {
            return null;
        }

        return loginEntity;
    }
}
