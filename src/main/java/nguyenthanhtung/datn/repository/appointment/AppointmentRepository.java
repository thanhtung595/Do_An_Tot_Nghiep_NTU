package nguyenthanhtung.datn.repository.appointment;


import nguyenthanhtung.datn.dto.appointment.request.RequestSaveAppointmentRecordDTO;
import nguyenthanhtung.datn.dto.appointment.request.RequestUpdateStatusAppointment;

import java.util.List;
import java.util.Map;

public interface AppointmentRepository {
    List<Map<String, Object>> getAppointmentsByUser();
    List<Map<String, Object>> getPatientHistory();
    Map<String, Object> getAppointmentRecordById(int idAppointment);
    void updateStatusAppointment(RequestUpdateStatusAppointment requestUpdateStatusAppointment);
    boolean saveMedicalRecord(RequestSaveAppointmentRecordDTO dto);
}
