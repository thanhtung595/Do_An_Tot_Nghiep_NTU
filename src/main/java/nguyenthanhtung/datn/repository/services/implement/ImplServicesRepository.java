package nguyenthanhtung.datn.repository.services.implement;

import lombok.RequiredArgsConstructor;
import nguyenthanhtung.datn.constants.ConstantQuery.medicines.ConstantQueryMedicines;
import nguyenthanhtung.datn.constants.ConstantQuery.services.ConstantQueryServices;
import nguyenthanhtung.datn.dto.services.UpdateServiceRequestDTO;
import nguyenthanhtung.datn.repository.services.ServicesRepository;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ImplServicesRepository implements ServicesRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Map<String, Object>> getServices() {
        return jdbcTemplate.query(ConstantQueryServices.SELECT_ALL_SERVICE, new ColumnMapRowMapper());
    }

    @Override
    public List<Map<String, Object>> getUseServiceByIdPatient(int id) {
        return jdbcTemplate.query(ConstantQueryServices.GET_USE_SERVICES_BY_ID_PATIENT_1, new ColumnMapRowMapper(), id);
    }

    @Override
    public List<String> getListNameServiceInvoiceByIdPatient(int id) {
        return jdbcTemplate.query(ConstantQueryServices.GET_LIST_NAME_SERVICE_INVOICE_BY_ID_PATIENT,
                new Object[]{id},
                (rs, rowNum) -> rs.getString("name"));
    }

    @Override
    public List<Map<String, Object>> getListServiceInvoiceByIdPatient(int id) {
        return jdbcTemplate.query(ConstantQueryServices.GET_LIST_SERVICE_INVOICE_BY_ID_PATIENT, new ColumnMapRowMapper(), id);
    }

    @Override
    public void update(UpdateServiceRequestDTO updateServiceRequestDTO) {
        jdbcTemplate.update(ConstantQueryServices.UPDATE,
                updateServiceRequestDTO.getName(),
                updateServiceRequestDTO.getDescription(),
                updateServiceRequestDTO.getPrice(),
                updateServiceRequestDTO.getId());
    }
}
