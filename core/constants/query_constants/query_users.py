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

GET_USER_BY_USERNAME_PASSWORDHASH = """
    SELECT 
        u.ID
        ,u.Username
        ,u.PasswordHash
        ,r.RoleName 
    FROM Users u
        INNER JOIN Roles r
        ON u.RoleID = r.ID
    WHERE u.Username = %s
"""

CHECK_USER_REGISTER_EXIT = """
    SELECT COUNT(*) 
    FROM Users
    WHERE Username = %s
"""

REGISTER_USER_BY_USERNAME_PASSWORDHASH = """
    INSERT INTO Users(Username, PasswordHash, RoleID) VALUES
    (%s, %s, 3)
    RETURNING ID
"""

REGISTER_DOCTOR_BY_USERNAME_PASSWORDHASH = """
    INSERT INTO Users(Username, PasswordHash, RoleID) VALUES
    (%s, %s, 2)
    RETURNING ID
"""

UPDATE_USER_BY_ID = """
    UPDATE Users
    SET roleid = (
        SELECT id FROM Roles WHERE rolename = 'admin'
    )
    WHERE id = 35;
"""