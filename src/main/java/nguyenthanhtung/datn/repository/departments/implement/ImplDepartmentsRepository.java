package nguyenthanhtung.datn.repository.departments.implement;

import lombok.RequiredArgsConstructor;
import nguyenthanhtung.datn.constants.ConstantQuery.departments.ConstantQueryDepartments;
import nguyenthanhtung.datn.repository.departments.DepartmentsRepository;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ImplDepartmentsRepository implements DepartmentsRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Map<String, Object>> getDepartments() {
        return jdbcTemplate.query(ConstantQueryDepartments.SELECT_ALL, new ColumnMapRowMapper());
    }
}
