package nguyenthanhtung.datn.controller.header;

import lombok.RequiredArgsConstructor;
import nguyenthanhtung.datn.dto.base.BaseResponse;
import nguyenthanhtung.datn.service.header.HeaderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/header")
@RequiredArgsConstructor
public class HeaderController {

    private final HeaderService headerService;

    @GetMapping()
    public ResponseEntity<BaseResponse> getHeader() {
        String subject = "@Get Header";

        BaseResponse response = headerService.getHeader();
        response.setSubjectFunction(subject);

        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<BaseResponse> getHeaderAdmin() {
        String subject = "@Get Header Admin";

        BaseResponse response = headerService.getHeaderAdmin();
        response.setSubjectFunction(subject);

        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }
}
