SELECT_ALL_USER = """
    SELECT
        u.id AS id,
        u.username AS name,
        r.rolename AS role,
        COALESCE(d.fullname, p.fullname, 'Nguyễn Thanh Tùng') AS fullname,
        COALESCE(d.email, p.email, 'nguyenthanhtung@gmail.com') AS email,
        COALESCE(d.phonenumber, p.phonenumber, '0836818595') AS phonenumber
    FROM 
    USERS u
    INNER JOIN ROLES r ON u.roleid = r.id
    LEFT JOIN doctors d ON d.userid = u.id
    LEFT JOIN patients p ON p.userid = u.id;
"""

SELECT_USER_BY_ID = """
    SELECT * FROM USERS 
        INNER JOIN PATIENTS
        ON USERS.ID = PATIENTS.UserID
        INNER JOIN ROLES
        ON USERS.RoleID = ROLES.ID
    WHERE USERS.ID = %s
"""

SELECT_USER_DOCTOR_BY_ID = """
    SELECT * FROM USERS 
        INNER JOIN DOCTORS
        ON USERS.ID = DOCTORS.UserID
        INNER JOIN ROLES
        ON USERS.RoleID = ROLES.ID 
    WHERE USERS.ID = %s
"""