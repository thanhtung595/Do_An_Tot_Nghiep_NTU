package nguyenthanhtung.datn.service.departments.implement;

import lombok.RequiredArgsConstructor;
import nguyenthanhtung.datn.constants.ContantApplication;
import nguyenthanhtung.datn.dto.base.BaseResponse;
import nguyenthanhtung.datn.repository.departments.DepartmentsRepository;
import nguyenthanhtung.datn.service.departments.DepartmentsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImplDepartmentsService implements DepartmentsService {

    private final DepartmentsRepository departmentsRepository;

    @Override
    public BaseResponse getDepartments() {
        BaseResponse response = new BaseResponse();
        try {
            List<Map<String, Object>> data = departmentsRepository.getDepartments();

            response.setHttpStatusCode(ContantApplication.HttpStatus_OK);
            response.setStatus(ContantApplication.StatusSuccess);
            response.setMessage(ContantApplication.MessageSuccess);
            response.addData("departments", data);
        } catch (Exception e) {
            response.setHttpStatusCode(ContantApplication.HttpStatus_Internal_Server_Error);
            response.setStatus(ContantApplication.StatusErrorBugException);
            response.setMessage(e.getMessage());
        }
        return response;
    }
}
