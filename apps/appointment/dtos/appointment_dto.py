class AppointmentUpdate:
    def __init__(self, data):
        self.idDoctor = data.get("id", 0)
        self.appointmentid = data.get("appointmentid", 0)
        self.medicalrecordsid = data.get("medicalrecordsid", 0)
        self.notes = data.get("notes", "")
        self.status = data.get("status", "")
        self.diagnosis = data.get("diagnosis", "")
        self.symptom = data.get("symptom", "")

    def __str__(self):
        return (
            f"AppointmentUpdate(\n"
            f"  idDoctor: {self.idDoctor},\n"
            f"  appointmentid: {self.appointmentid},\n"
            f"  medicalrecordsid: {self.medicalrecordsid},\n"
            f"  notes: '{self.notes}',\n"
            f"  status: '{self.status}',\n"
            f"  diagnosis: '{self.diagnosis}',\n"
            f"  symptom: '{self.symptom}'\n"
            f")"
        )
