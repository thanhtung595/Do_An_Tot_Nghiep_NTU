package nguyenthanhtung.datn.service.services;

import nguyenthanhtung.datn.dto.base.BaseResponse;
import nguyenthanhtung.datn.dto.services.UpdateServiceRequestDTO;

public interface ServicesService {
    BaseResponse getServices();
    BaseResponse getListNameServiceInvoiceByIdPatient(int id);
    BaseResponse update(UpdateServiceRequestDTO updateServiceRequestDTO);
}
