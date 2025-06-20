package nguyenthanhtung.datn.repository.header.implement;

import lombok.RequiredArgsConstructor;
import nguyenthanhtung.datn.config.requestThreadLocal.RequestThreadLocalContext;
import nguyenthanhtung.datn.config.requestThreadLocal.RequestUserThreadLocal;
import nguyenthanhtung.datn.constants.ConstantQuery.header.ConstantQueryHeader;
import nguyenthanhtung.datn.constants.ContantApplication;
import nguyenthanhtung.datn.repository.header.HeaderRepository;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class ImplHeaderRepository implements HeaderRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Map<String, Object>> getHeader() {
        RequestUserThreadLocal currentUser = RequestThreadLocalContext.get();
        if (currentUser != null && Objects.equals(currentUser.getRole(), ContantApplication.RoleAdmin)){
            return jdbcTemplate.query(ConstantQueryHeader.GET_ALL_HEADER_ADMIN, new ColumnMapRowMapper());
        }else if (currentUser != null && Objects.equals(currentUser.getRole(), ContantApplication.RoleDoctor)){
            return jdbcTemplate.query(ConstantQueryHeader.GET_ALL_HEADER_DOCTOR, new ColumnMapRowMapper());
        } else if (currentUser != null && Objects.equals(currentUser.getRole(), ContantApplication.RolePatient)){
            return jdbcTemplate.query(ConstantQueryHeader.GET_ALL_HEADER_PATIENT, new ColumnMapRowMapper());
        }else {
            return jdbcTemplate.query(ConstantQueryHeader.GET_ALL_HEADER, new ColumnMapRowMapper());
        }
    }

    @Override
    public List<Map<String, Object>> getHeaderAdmin() {
        return jdbcTemplate.query(ConstantQueryHeader.GET_ALL_PAGE_HEADER_ADMIN, new ColumnMapRowMapper());
    }
}
