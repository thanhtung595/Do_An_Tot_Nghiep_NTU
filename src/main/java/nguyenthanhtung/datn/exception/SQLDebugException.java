package nguyenthanhtung.datn.exception;

public class SQLDebugException extends Exception {

    // Constructor không tham số
    public SQLDebugException() {
        super();
    }

    // Constructor với message
    public SQLDebugException(String message) {
        super(message);
    }

    // Constructor với message và nguyên nhân
    public SQLDebugException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructor với nguyên nhân
    public SQLDebugException(Throwable cause) {
        super(cause);
    }
}
