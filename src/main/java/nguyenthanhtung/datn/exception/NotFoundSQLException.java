package nguyenthanhtung.datn.exception;

public class NotFoundSQLException extends Exception{

    // Constructor không tham số
    public NotFoundSQLException() {
        super();
    }

    // Constructor với message
    public NotFoundSQLException(String message) {
        super(message);
    }

    // Constructor với message và nguyên nhân
    public NotFoundSQLException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructor với nguyên nhân
    public NotFoundSQLException(Throwable cause) {
        super(cause);
    }
}
