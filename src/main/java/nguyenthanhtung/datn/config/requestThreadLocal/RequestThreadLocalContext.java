package nguyenthanhtung.datn.config.requestThreadLocal;

public class RequestThreadLocalContext {

    private static final ThreadLocal<RequestUserThreadLocal> requestUserThreadLocal = new ThreadLocal<>();

    public static void set(RequestUserThreadLocal userThreadLocal) {
        requestUserThreadLocal.set(userThreadLocal);
    }

    public static RequestUserThreadLocal get() {
        return requestUserThreadLocal.get();
    }

    public static void clear() {
        requestUserThreadLocal.remove();
    }

}
