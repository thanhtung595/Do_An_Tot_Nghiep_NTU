package nguyenthanhtung.datn.dto.notification.request;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class CreateNotificationDTO {
    String Title;
    String Content;
    int IdType;
    LocalDate AppointmentDate;
    String AppointmentTime;
    int PatientID;
    int Doctorid;
    String nameRole;
    String status;
    String userfrom;
    String userto;
}
