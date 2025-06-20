package nguyenthanhtung.datn.repository.invoice.implement;

import lombok.RequiredArgsConstructor;
import nguyenthanhtung.datn.constants.ConstantQuery.invoice.ConstantQueryInvoice;
import nguyenthanhtung.datn.repository.invoice.InvoiceRepository;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ImplInvoiceRepository implements InvoiceRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Map<String, Object>> getInvoiceByIdUser(Integer idUser) {
        return jdbcTemplate.query(ConstantQueryInvoice.GET_ALL_INVOICE, new ColumnMapRowMapper(), idUser);
    }

    @Override
    public Map<String, Object> getInvoiceByIdUserAndInvoice(Integer idUser, int idInvoice) {
        List<Map<String, Object>> data = jdbcTemplate.query(ConstantQueryInvoice.GET_BY_ID_INVOICE, new ColumnMapRowMapper(), idUser, idInvoice);
        return data.getFirst();
    }

    @Override
    public void updateStatusInvoice(int id, String status) {
        jdbcTemplate.update(ConstantQueryInvoice.UPDATE_STATUS_INVOICE, status, id);
    }

    @Override
    public List<Map<String, Object>> getInvoiceById(int id) {
        return jdbcTemplate.query(ConstantQueryInvoice.GET__INVOICE_BY_ID, new ColumnMapRowMapper(), id);
    }

}
