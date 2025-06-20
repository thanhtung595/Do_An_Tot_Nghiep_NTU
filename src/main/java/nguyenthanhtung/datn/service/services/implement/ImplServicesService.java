package nguyenthanhtung.datn.service.services.implement;

import lombok.RequiredArgsConstructor;
import nguyenthanhtung.datn.constants.ContantApplication;
import nguyenthanhtung.datn.dto.base.BaseResponse;
import nguyenthanhtung.datn.dto.services.UpdateServiceRequestDTO;
import nguyenthanhtung.datn.repository.services.ServicesRepository;
import nguyenthanhtung.datn.service.services.ServicesService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImplServicesService implements ServicesService {

    private final ServicesRepository servicesRepository;

    @Override
    public BaseResponse getServices() {
        BaseResponse response = new BaseResponse();
        try {
            List<Map<String, Object>> data = servicesRepository.getServices();
            for (Map<String, Object> item : data){
                if (item.containsKey("image")) {
                    item.compute("image", (k, oldValue) -> System.getProperty("URL_AWS_STORAGE") + oldValue);
                }
            }

            response.setHttpStatusCode(ContantApplication.HttpStatus_OK);
            response.setStatus(ContantApplication.StatusSuccess);
            response.setMessage(ContantApplication.MessageSuccess);
            response.addData("service", data);
        } catch (Exception e) {
            response.setHttpStatusCode(ContantApplication.HttpStatus_Internal_Server_Error);
            response.setStatus(ContantApplication.StatusErrorBugException);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public BaseResponse getListNameServiceInvoiceByIdPatient(int id) {
        BaseResponse response = new BaseResponse();
        try {
            List<String> data = servicesRepository.getListNameServiceInvoiceByIdPatient(id);

            response.setHttpStatusCode(ContantApplication.HttpStatus_OK);
            response.setStatus(ContantApplication.StatusSuccess);
            response.setMessage(ContantApplication.MessageSuccess);
            response.addData("services", data);
        } catch (Exception e) {
            response.setHttpStatusCode(ContantApplication.HttpStatus_Internal_Server_Error);
            response.setStatus(ContantApplication.StatusErrorBugException);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public BaseResponse update(UpdateServiceRequestDTO updateServiceRequestDTO) {
        BaseResponse response = new BaseResponse();
        try {
            servicesRepository.update(updateServiceRequestDTO);

            response.setHttpStatusCode(ContantApplication.HttpStatus_OK);
            response.setStatus(ContantApplication.StatusSuccess);
            response.setMessage(ContantApplication.MessageSuccess);
        } catch (Exception e) {
            response.setHttpStatusCode(ContantApplication.HttpStatus_Internal_Server_Error);
            response.setStatus(ContantApplication.StatusErrorBugException);
            response.setMessage(e.getMessage());
        }
        return response;
    }
}
