package nguyenthanhtung.datn.controller.departments;

import lombok.RequiredArgsConstructor;
import nguyenthanhtung.datn.dto.base.BaseResponse;
import nguyenthanhtung.datn.service.departments.DepartmentsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentsContoller {

    private final DepartmentsService departmentsService;

    @GetMapping()
    public ResponseEntity<BaseResponse> getDepartment() {
        String subject = "@Get Department";

        BaseResponse response = departmentsService.getDepartments();
        response.setSubjectFunction(subject);

        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }

}
