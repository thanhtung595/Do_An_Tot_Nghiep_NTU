CREATE_PATIENTS = """
    INSERT INTO PATIENTS(FullName, DateOfBirth, Gender, PhoneNumber, Email, Address, UserID, Image) VALUES
    (%s, NOW(), 'Khác', %s, %s, '', %s, 'img/patients/avatar_default.jpg')
    RETURNING ID
"""