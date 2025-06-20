package nguyenthanhtung.datn.constants.ConstantQuery.auth;

public class ConstantQueryAuth {

    public static final String CHECK_USER_REGISTER_EXIT = """
        (SELECT 'username' AS field FROM Users WHERE username = :username LIMIT 1)
        UNION ALL
        (SELECT 'email' FROM Users WHERE email = :email LIMIT 1)
        UNION ALL
        (SELECT 'phonenumber' FROM Users WHERE phonenumber = :phonenumber LIMIT 1);
    """;

    public static final String CREATE_USER = """
        INSERT INTO "users" ("username", "passwordhash", "fullname", "phonenumber", "email", "address", "image", "gender", "roleid")
        VALUES (:username, :passwordhash, :fullname, :phonenumber, :email, :address, :image, :gender, :roleid)
        RETURNING ID;
    """;

    public static final String LOGIN_BY_USERNAME = """
        SELECT
          us.id,
          us.username,
          us.passwordhash,
          rl.rolename role
        FROM users us
          INNER JOIN roles rl
            ON us.roleid = rl.id
        WHERE
            username = :username;
    """;
}
