package nguyenthanhtung.datn.util;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import static nguyenthanhtung.datn.util.JdbcUtils.safeQueryForSingleId;

@Component
@RequiredArgsConstructor
public class IdQueryUserUtils {

    private final JdbcTemplate jdbcTemplate;

    public Integer getIdUserByDoctor(int idDoctor){
        String query = "SELECT userid FROM doctors WHERE id = ?;";
        return safeQueryForSingleId(jdbcTemplate, query, new Object[]{idDoctor});
    }

    public Integer getIdUserByPatient(int idPatient){
        String query = "SELECT userid FROM patients WHERE id = ?;";
        return safeQueryForSingleId(jdbcTemplate, query, new Object[]{idPatient});
    }
}
