package nguyenthanhtung.datn.service.doctor;

import nguyenthanhtung.datn.dto.base.BaseResponse;
import nguyenthanhtung.datn.dto.doctor.request.RequestDoctor;
import nguyenthanhtung.datn.dto.doctor.request.RequestDoctorAppointment;

public interface DoctorService {
    BaseResponse getAllDoctor();
    BaseResponse createDoctor(RequestDoctor requestDoctor);
    BaseResponse updateDoctor(RequestDoctor requestDoctor);
    BaseResponse bookAppointment(RequestDoctorAppointment requestDoctorAppointment);
}
