package nguyenthanhtung.datn.constants;

public class ContantApplication {

    // Jwt
    public static String ClaimUserID = "userID";
    public static String ClaimRoleUser = "role";

    // Role
    public static String HasRoleAdmin = "ADMIN";
    public static String HasRoleDoctor = "DOCTOR";
    public static String HasRolePatient = "PATIENT";
    public static String RoleAdmin = "admin";
    public static String RoleDoctor = "doctor";
    public static String RolePatient = "patient";

    // Message
    public static String MessageSuccess = "Success";
    public static String MessageError = "Error";

    // Status Application
    public static String StatusErrorBugException = "16";
    public static String StatusErrorClient = "17";
    public static String StatusErrorServer = "18";
    public static String StatusErrorBugSQL = "19";
    public static String StatusErrorServer_File = "20";
    public static String StatusSuccess = "0";
    public static String StatusSuccess_No_Data = "05";
    public static String StatusWarning_Conflict = "11";

    // Status code http
    public static int HttpStatus_OK = 200;
    public static int HttpStatus_CREATED = 201;
    public static int HttpStatus_NO_Accepted= 202;
    public static int HttpStatus_NO_CONTENT = 204;
    public static int HttpStatus_RESET_CONTENT = 205;
    public static int HttpStatus_BAD_REQUEST = 400;
    public static int HttpStatus_Unauthorized = 401;
    public static int HttpStatus_Forbidden = 403;
    public static int HttpStatus_NOT_FOUND = 404;
    public static int HttpStatus_Method_Not_Allowed = 405;
    public static int HttpStatus_Conflict = 409;
    public static int HttpStatus_Unprocessable_Entity = 422;
    public static int HttpStatus_Too_Many_Requests = 429;
    public static int HttpStatus_Internal_Server_Error = 500;
    public static int HttpStatus_Not_Implemented = 501;
    public static int HttpStatus_Bad_Gateway = 502;
    public static int HttpStatus_Service_Unavailable = 503;
    public static int HttpStatus_Gateway_Timeout = 504;
    public static int HttpStatus_HTTP_Version_Not_Supported = 505;

}
