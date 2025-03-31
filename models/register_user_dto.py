class RegisterUserDTO:
    def __init__(self, data):
        self.username = data.get("username")
        self.email = data.get("email")
        self.fullname = data.get("fullname")
        self.phone = data.get("phone")
        self.password = data.get("password")
