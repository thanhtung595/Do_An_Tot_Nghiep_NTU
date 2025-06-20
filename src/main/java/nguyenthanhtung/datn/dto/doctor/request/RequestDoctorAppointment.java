package nguyenthanhtung.datn.dto.doctor.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RequestDoctorAppointment {
    int userID;
    int doctorID;
    String doctorName;
    String diagnosis;
    String workday;
    String workdayConvert;
    String timeOnline;
    String note;
    String fullname;
    LocalDate age;
    String gender;
    String address;
    String phonenumber;
}
