class DoctorDTO:
    def __init__(self, data):
        self.idDoctor = data.get("id", 0)
        self.username = data.get("username", "")
        self.password = data.get("password", "")
        self.email = data.get("email", "")
        self.fullname = data.get("fullname", "")
        self.phone = data.get("phonenumber", "")
        self.address = data.get("address", "")
        self.userID = data.get("userID", None)
        self.departmentID = data.get("departmentid", None)
        self.experience = data.get("experience", 0)
        self.timeonline = data.get("timeonline", 0)
        self.workdays = data.get("workdays", 0)

    def __str__(self):
        return (
            f"DoctorDTO("
            f"idDoctor={self.idDoctor}, "
            f"username={self.username}, "
            f"email={self.email}, "
            f"fullname={self.fullname}, "
            f"phone={self.phone}, "
            f"address={self.address}, "
            f"userID={self.userID}, "
            f"departmentID={self.departmentID}, "
            f"experience={self.experience}, "
            f"timeonline={self.timeonline}, "
            f"workdays={self.workdays}), "
        )
