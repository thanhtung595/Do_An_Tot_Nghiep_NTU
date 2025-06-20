package nguyenthanhtung.datn.repository.services;

import nguyenthanhtung.datn.dto.services.UpdateServiceRequestDTO;

import java.util.List;
import java.util.Map;

public interface ServicesRepository {
    List<Map<String, Object>> getServices();
    List<Map<String, Object>> getUseServiceByIdPatient(int id);
    List<String> getListNameServiceInvoiceByIdPatient(int id);
    List<Map<String, Object>> getListServiceInvoiceByIdPatient(int id);
    void update(UpdateServiceRequestDTO updateServiceRequestDTO);
}
