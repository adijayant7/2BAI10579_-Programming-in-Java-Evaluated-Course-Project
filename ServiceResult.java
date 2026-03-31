package banking.service;

/**
 * Wraps the outcome of a service call.
 * The UI reads {@code isSuccess()} and {@code getMessage()} to show alerts.
 */
public class ServiceResult {

    private final boolean success;
    private final String  message;
    private final String  data;      // optional payload (e.g. new account number)

    private ServiceResult(boolean success, String message, String data) {
        this.success = success;
        this.message = message;
        this.data    = data;
    }

    // ── Factory methods ───────────────────────────────────────────────────

    public static ServiceResult success(String message) {
        return new ServiceResult(true, message, null);
    }

    public static ServiceResult success(String message, String data) {
        return new ServiceResult(true, message, data);
    }

    public static ServiceResult fail(String message) {
        return new ServiceResult(false, message, null);
    }

    // ── Accessors ─────────────────────────────────────────────────────────

    public boolean isSuccess()  { return success; }
    public String  getMessage() { return message; }
    public String  getData()    { return data; }
}
