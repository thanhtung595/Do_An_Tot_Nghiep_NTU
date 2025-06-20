package nguyenthanhtung.datn.service.appointment;

import nguyenthanhtung.datn.dto.appointment.request.RequestSaveAppointmentRecordDTO;
import nguyenthanhtung.datn.dto.appointment.request.RequestUpdateStatusAppointment;
import nguyenthanhtung.datn.dto.base.BaseResponse;

public interface AppointmentService {
    BaseResponse getAppointmentsByUser();
    BaseResponse getPatientHistory();
    BaseResponse updateStatusAppointment(RequestUpdateStatusAppointment requestUpdateStatusAppointment);
    BaseResponse getAppointmentRecordById(int idAppointment);
    BaseResponse saveMedicalRecord(RequestSaveAppointmentRecordDTO dto);
}
