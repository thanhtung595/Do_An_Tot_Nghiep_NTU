package nguyenthanhtung.datn.util;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

public class JdbcUtils {

    /**
     * Thực hiện truy vấn SQL trả về duy nhất 1 ID (hoặc null nếu không có)
     *
     * @param jdbcTemplate          NamedParameterJdbcTemplate bên ngoài truyền vào
     * @param sql                   Câu SQL cần truy vấn
     * @param params                Các tham số truy vấn
     * @return                      ID đầu tiên hoặc null nếu không có
     */
    public static Integer safeQueryForSingleId(NamedParameterJdbcTemplate jdbcTemplate,
                                               String sql,
                                               MapSqlParameterSource params) {
        List<Integer> ids = jdbcTemplate.query(
                sql,
                params,
                (rs, rowNum) -> rs.getInt("id")
        );
        return ids.isEmpty() ? null : ids.getFirst();
    }

    public static Integer safeQueryForSingleId(NamedParameterJdbcTemplate jdbcTemplate,
                                               String sql) {
        List<Integer> ids = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> rs.getInt("id")
        );
        return ids.isEmpty() ? null : ids.getFirst();
    }

    // Dùng JdbcTemplate và Object[] args
    public static Integer safeQueryForSingleId(JdbcTemplate jdbcTemplate,
                                               String sql,
                                               Object[] params) {
        List<Integer> ids = jdbcTemplate.query(
                sql,
                params,
                (rs, rowNum) -> rs.getInt(1) // lấy cột đầu tiên
        );
        return ids.isEmpty() ? null : ids.getFirst();
    }

    // Dùng JdbcTemplate và Object[] args not params
    public static Integer safeQueryForSingleId(JdbcTemplate jdbcTemplate,
                                               String sql) {
        List<Integer> ids = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> rs.getInt(1) // lấy cột đầu tiên
        );
        return ids.isEmpty() ? null : ids.getFirst();
    }
}
