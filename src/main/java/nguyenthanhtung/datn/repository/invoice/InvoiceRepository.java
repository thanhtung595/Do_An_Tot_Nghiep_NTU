package nguyenthanhtung.datn.repository.invoice;

import java.util.List;
import java.util.Map;

public interface InvoiceRepository {
    List<Map<String, Object>> getInvoiceByIdUser(Integer idUser);
    Map<String, Object> getInvoiceByIdUserAndInvoice(Integer idUser, int idInvoice);
    void updateStatusInvoice(int id, String status);
    List<Map<String, Object>> getInvoiceById(int id);

}
