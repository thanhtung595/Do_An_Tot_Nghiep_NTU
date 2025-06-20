package nguyenthanhtung.datn.service.medicines.implement;

import lombok.RequiredArgsConstructor;
import nguyenthanhtung.datn.constants.ContantApplication;
import nguyenthanhtung.datn.dto.base.BaseResponse;
import nguyenthanhtung.datn.repository.medicines.MedicinesRepository;
import nguyenthanhtung.datn.service.medicines.MedicinesService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImplMedicinesService implements MedicinesService {

    private final MedicinesRepository medicinesRepository;

    @Override
    public BaseResponse getMedicines() {
        BaseResponse response = new BaseResponse();
        try {
            List<Map<String, Object>> data = medicinesRepository.getMedicines();

            response.setHttpStatusCode(ContantApplication.HttpStatus_OK);
            response.setStatus(ContantApplication.StatusSuccess);
            response.setMessage(ContantApplication.MessageSuccess);
            response.addData("medicines", data);
        } catch (Exception e) {
            response.setHttpStatusCode(ContantApplication.HttpStatus_Internal_Server_Error);
            response.setStatus(ContantApplication.StatusErrorBugException);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public BaseResponse getListNameMedicinesInvoiceByIdPatient(int id) {
        BaseResponse response = new BaseResponse();
        try {
            List<String> data = medicinesRepository.getListNameMedicinesInvoiceByIdPatient(id);

            response.setHttpStatusCode(ContantApplication.HttpStatus_OK);
            response.setStatus(ContantApplication.StatusSuccess);
            response.setMessage(ContantApplication.MessageSuccess);
            response.addData("medicines", data);
        } catch (Exception e) {
            response.setHttpStatusCode(ContantApplication.HttpStatus_Internal_Server_Error);
            response.setStatus(ContantApplication.StatusErrorBugException);
            response.setMessage(e.getMessage());
        }
        return response;
    }

}
