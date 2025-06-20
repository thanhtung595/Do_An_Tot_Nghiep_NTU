package nguyenthanhtung.datn.dto.doctor.request;

import lombok.Data;

@Data
public class RequestDoctor {
    int id = 0;
    String username;
    String password;
    String email;
    String fullname;
    String phonenumber;
    String address;
    int userID;
    int departmentid;
    int experience;
    String timeonline;
    String workdays;
}
