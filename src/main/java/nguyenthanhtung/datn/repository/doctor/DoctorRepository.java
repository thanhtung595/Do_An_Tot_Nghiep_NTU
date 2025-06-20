package nguyenthanhtung.datn.repository.doctor;

import nguyenthanhtung.datn.dto.doctor.request.RequestDoctor;
import nguyenthanhtung.datn.dto.doctor.request.RequestDoctorAppointment;
import nguyenthanhtung.datn.entity.base.RepositoryResult;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public interface DoctorRepository {
    RepositoryResult createDoctor(RequestDoctor requestDoctor, List<String> listDay, List<LocalTime> listTime);
    RepositoryResult updateDoctor(RequestDoctor requestDoctor, List<String> listDay, List<LocalTime> listTime);
    List<Map<String, Object>> getAllDoctor();
    RepositoryResult bookAppointment(RequestDoctorAppointment requestDoctorAppointment);
}
