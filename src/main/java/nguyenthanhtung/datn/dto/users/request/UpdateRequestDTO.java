package nguyenthanhtung.datn.dto.users.request;

import lombok.Data;

@Data
public class UpdateRequestDTO {
    int id;
    String fullname;
    String gender;
    String phonenumber;
    String address;
}
