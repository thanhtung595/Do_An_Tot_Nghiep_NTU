package nguyenthanhtung.datn.exception;

public class ConflictException extends Exception{
    
    // Constructor không tham số
    public ConflictException() {
        super();
    }

    // Constructor với message
    public ConflictException(String message) {
        super(message);
    }

    // Constructor với message và nguyên nhân
    public ConflictException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructor với nguyên nhân
    public ConflictException(Throwable cause) {
        super(cause);
    }
}
