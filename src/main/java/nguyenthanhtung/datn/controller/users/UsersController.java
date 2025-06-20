package nguyenthanhtung.datn.controller.users;

import lombok.RequiredArgsConstructor;
import nguyenthanhtung.datn.dto.base.BaseResponse;
import nguyenthanhtung.datn.dto.users.request.EditPasswordRequestDTO;
import nguyenthanhtung.datn.dto.users.request.UpdateRequestDTO;
import nguyenthanhtung.datn.service.users.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @GetMapping()
    public ResponseEntity<BaseResponse> getProfile() {
        String subject = "@Get Get Profile";

        BaseResponse response = usersService.getProfile();
        response.setSubjectFunction(subject);

        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }

    @PutMapping("edit-password")
    public ResponseEntity<BaseResponse> editPassword(@RequestBody EditPasswordRequestDTO editPasswordRequestDTO) {
        String subject = "@Put Edit Password";

        BaseResponse response = usersService.editPassword(editPasswordRequestDTO);
        response.setSubjectFunction(subject);

        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }

    @PutMapping("update-id")
    public ResponseEntity<BaseResponse> updateUser(@RequestBody UpdateRequestDTO updateRequestDTO) {
        String subject = "@Put Update User";

        BaseResponse response = usersService.updateUser(updateRequestDTO);
        response.setSubjectFunction(subject);

        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<BaseResponse> getAllUser() {
        String subject = "@Get Get All User";

        BaseResponse response = usersService.getAllUser();
        response.setSubjectFunction(subject);

        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }

    @PostMapping("/upload")
    public ResponseEntity<BaseResponse> uploadImage(@RequestParam("file") MultipartFile file) {
        String subject = "@Get Upload Image";

        BaseResponse response = usersService.uploadImage(file);
        response.setSubjectFunction(subject);

        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }
}
