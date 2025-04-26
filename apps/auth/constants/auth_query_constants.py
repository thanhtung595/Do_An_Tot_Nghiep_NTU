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

