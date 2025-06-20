package nguyenthanhtung.datn.dto.appointment.request;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class RequestSaveAppointmentRecordDTO {
    int id;
    int patientid;
    int doctorid;
    String patientname;
    LocalDate nextappointment;
    String status;
    String diagnosis;
    String symptom;
    String treatmentplan;
    List<RecordMedicinesDTO> medicines;
    List<RecordServicesDTO> services;
}
