class Appointment:
     def __init__(self, data):
          self.idDoctor = data.get("id", 0)
          self.doctorID = data.get("doctorID", "")
          self.diagnosis = data.get("diagnosis", "")
          self.workday = data.get("workday", "")
          self.timeOnline = data.get("timeOnline", "")
          self.note = data.get("note", "")