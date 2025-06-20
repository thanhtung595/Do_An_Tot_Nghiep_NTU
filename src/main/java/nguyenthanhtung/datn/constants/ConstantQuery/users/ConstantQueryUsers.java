package nguyenthanhtung.datn.constants.ConstantQuery.users;

public class ConstantQueryUsers {

    public static final String SELECT_ALL_USER = """
        SELECT
            u.id AS id,
            u.username AS name,
            r.rolename AS role,
            u.fullname AS fullname,
            u.email AS email,
            u.phonenumber AS phonenumber
        FROM
        USERS u
            INNER JOIN ROLES r ON u.roleid = r.id;
    """;

    public static final String SELECT_USER_BY_ID = """
    SELECT * FROM USERS
        INNER JOIN ROLES
        ON USERS.RoleID = ROLES.ID
    WHERE USERS.ID = ?
    """;

    public static final String SELECT_USER_DOCTOR_BY_ID = """
        SELECT * FROM USERS
            INNER JOIN ROLES
            ON USERS.RoleID = ROLES.ID
        WHERE USERS.ID = ?
    """;

    public static final String UPDATE_USER_BY_ID = """
        UPDATE USERS
        SET fullname = ?, gender = ?, phonenumber = ?, address = ?
        WHERE id = ?;
    """;

    public static final String GET_USER_BY_ID = """
        SELECT * FROM users
        WHERE id = ?
        LIMIT 1;
    """;

    public static final String UPDATE_PASS = """
        UPDATE users
        SET passwordhash = ?
        WHERE id = ?;
    """;

    public static final String UPDATE_IMAGE = """
        UPDATE users
        SET image = ?
        WHERE id = ?;
    """;
}
