package nguyenthanhtung.datn.repository.appointment.implement;

import lombok.RequiredArgsConstructor;
import nguyenthanhtung.datn.config.requestThreadLocal.RequestThreadLocalContext;
import nguyenthanhtung.datn.config.requestThreadLocal.RequestUserThreadLocal;
import nguyenthanhtung.datn.constants.ConstantQuery.appointment.ConstantQueryAppointment;
import nguyenthanhtung.datn.constants.ConstantQuery.doctor.ConstantQueryDoctor;
import nguyenthanhtung.datn.constants.ConstantQuery.services.ConstantQueryServices;
import nguyenthanhtung.datn.constants.ContantApplication;
import nguyenthanhtung.datn.dto.appointment.request.RecordMedicinesDTO;
import nguyenthanhtung.datn.dto.appointment.request.RecordServicesDTO;
import nguyenthanhtung.datn.dto.appointment.request.RequestSaveAppointmentRecordDTO;
import nguyenthanhtung.datn.dto.appointment.request.RequestUpdateStatusAppointment;
import nguyenthanhtung.datn.repository.appointment.AppointmentRepository;
import nguyenthanhtung.datn.util.JdbcUtils;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class ImplAppointmentRepository implements AppointmentRepository {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<Map<String, Object>> getAppointmentsByUser() {
        RequestUserThreadLocal currentUser = RequestThreadLocalContext.get();
        List<Map<String, Object>> data = Collections.emptyList();

        if (Objects.equals(currentUser.getRole(), ContantApplication.RolePatient)){
            data = jdbcTemplate.query(ConstantQueryAppointment.SELECT_APPOINTMENTS_BY_ID_PATIENTS, new ColumnMapRowMapper(), currentUser.getUserId());
        }else if (Objects.equals(currentUser.getRole(), ContantApplication.RoleDoctor)){
            data = jdbcTemplate.query(ConstantQueryAppointment.SELECT_APPOINTMENTS_BY_ID_DOCTOR, new ColumnMapRowMapper(), currentUser.getUserId());
        }

        return data;
    }

    @Override
    public List<Map<String, Object>> getPatientHistory() {
        RequestUserThreadLocal currentUser = RequestThreadLocalContext.get();
        return jdbcTemplate.query(ConstantQueryAppointment.SELECT_PATIENT_HISTORY, new ColumnMapRowMapper(), currentUser.getUserId());
    }

    @Override
    public Map<String, Object> getAppointmentRecordById(int idAppointment) {
        List<Map<String, Object>> data = jdbcTemplate.query(ConstantQueryAppointment.GET_APPOINTMENT_RECODR_BY_ID, new ColumnMapRowMapper(), idAppointment);
        return data.isEmpty() ? null : data.getFirst();
    }

    @Override
    public void updateStatusAppointment(RequestUpdateStatusAppointment requestUpdateStatusAppointment) {
        jdbcTemplate.update(ConstantQueryAppointment.UPDATE_STATUS_APPOINTMENT,
                requestUpdateStatusAppointment.getStatus(),
                requestUpdateStatusAppointment.getId());
    }

    @Override
    public boolean saveMedicalRecord(RequestSaveAppointmentRecordDTO dto) {

        double totalAmount = 0.0;
        Integer idInvoice = 0;
        boolean result = false;

        List<Map<String, Object>> checkInvoicePayment =
                jdbcTemplate.query(ConstantQueryAppointment.CHECK_INCOICE_PAYMENT, new ColumnMapRowMapper(), dto.getPatientid());

        // Get total mount
        if (!dto.getServices().isEmpty()){
            for (RecordServicesDTO service : dto.getServices()){
                totalAmount += service.getPrice();
            }
        }

        if (!checkInvoicePayment.isEmpty()){
            Object idInvoiceObj = checkInvoicePayment.getFirst().get("id");
            idInvoice = (int) idInvoiceObj;

            Object isPayment = "";
            Object status = checkInvoicePayment.get(0).get("status");
            if (!Boolean.TRUE.equals(status)) {
                isPayment = status;
            }

            if (Objects.equals("Đã thanh toán", isPayment)){
                // Tao tham Create Invoice
                Map<String, Object> paramsCreateInvoice = new HashMap<>();
                paramsCreateInvoice.put("PatientID", dto.getPatientid());
                paramsCreateInvoice.put("TotalAmount", totalAmount);

                // Create Invoice
                idInvoice = namedParameterJdbcTemplate.queryForObject(
                        ConstantQueryAppointment.CREATE_INVOICE,
                        new MapSqlParameterSource(paramsCreateInvoice),
                        Integer.class
                );

                result = true;
            } else {
                // Delete use services
                jdbcTemplate.update(ConstantQueryAppointment.DELETE_USE_SERVICE, dto.getPatientid());

                // Delete Medicines Invoice
                jdbcTemplate.update(ConstantQueryAppointment.DELETE_INVOICE_MEDICIES, idInvoice);
            }
        }

        if (checkInvoicePayment.isEmpty())
        {
            // Tao tham Create Invoice
            Map<String, Object> paramsCreateInvoice = new HashMap<>();
            paramsCreateInvoice.put("PatientID", dto.getPatientid());
            paramsCreateInvoice.put("TotalAmount", totalAmount);

            // Create Invoice
            idInvoice = namedParameterJdbcTemplate.queryForObject(
                    ConstantQueryAppointment.CREATE_INVOICE,
                    new MapSqlParameterSource(paramsCreateInvoice),
                    Integer.class
            );

            result = true;
        }

        // Create use services
        if (!dto.getServices().isEmpty()){
            for (RecordServicesDTO service : dto.getServices()){
                jdbcTemplate.update(ConstantQueryServices.CREATE_SERVICES,
                        dto.getPatientid(), service.getServicesid(), service.getPrice());
            }
        }

        // Create Medicines Invoice
        if (!dto.getMedicines().isEmpty()){
            for (RecordMedicinesDTO recordMedicine : dto.getMedicines()){
                jdbcTemplate.update(ConstantQueryAppointment.CREATE_INVOICE_MEDICIES,
                        idInvoice, recordMedicine.getIdmedicines());
            }
        }

        // Update TotalAmount Invoice
        jdbcTemplate.update(ConstantQueryAppointment.UPDATE_TOTAL_MOUNT, totalAmount, idInvoice);

        // Update Appointments
        jdbcTemplate.update(ConstantQueryAppointment.UPDATE_MR_STATUS_CHECKAGAIN,
                dto.getStatus(), dto.getNextappointment(), dto.getId());

        // Update MedicalRecords
        jdbcTemplate.update(ConstantQueryAppointment.UPDATE_MR_DTS,
                dto.getDiagnosis(), dto.getTreatmentplan(), dto.getSymptom(), dto.getId());

        return result;
    }
}
