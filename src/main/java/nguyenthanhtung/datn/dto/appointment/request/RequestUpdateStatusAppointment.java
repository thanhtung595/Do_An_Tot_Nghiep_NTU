package nguyenthanhtung.datn.dto.appointment.request;

import lombok.Data;

@Data
public class RequestUpdateStatusAppointment {
    int id;
    int patientid;
    int doctorid;
    String status;
    String date;
    String time;
    String patientname;
    String doctorname;
}
