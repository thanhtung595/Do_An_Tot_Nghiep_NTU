package nguyenthanhtung.datn.repository.medicines.implement;

import lombok.RequiredArgsConstructor;
import nguyenthanhtung.datn.constants.ConstantQuery.medicines.ConstantQueryMedicines;
import nguyenthanhtung.datn.repository.medicines.MedicinesRepository;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ImplMedicinesRepository implements MedicinesRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Map<String, Object>> getMedicines() {
        return jdbcTemplate.query(ConstantQueryMedicines.SELECT_ALL, new ColumnMapRowMapper());
    }

    @Override
    public List<Map<String, Object>> getMedicinesInvoiceByIdPatient(int id) {
        return jdbcTemplate.query(ConstantQueryMedicines.GET_MEDICINES_INVOICE_BY_ID_PATIENT_1, new ColumnMapRowMapper(), id);
    }

    @Override
    public List<String> getListNameMedicinesInvoiceByIdPatient(int id) {
        return jdbcTemplate.query(ConstantQueryMedicines.GET_LIST_NAME_MEDICINES_INVOICE_BY_ID_PATIENT,
                new Object[]{id},
                (rs, rowNum) -> rs.getString("name"));
    }
}
