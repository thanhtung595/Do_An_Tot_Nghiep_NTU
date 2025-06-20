package nguyenthanhtung.datn.service.medicines;

import nguyenthanhtung.datn.dto.base.BaseResponse;

public interface MedicinesService {
    BaseResponse getMedicines();
    BaseResponse getListNameMedicinesInvoiceByIdPatient(int id);
}
