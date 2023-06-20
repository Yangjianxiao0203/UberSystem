package ubersystem.Result;

public enum ResponseStatus {
    SUCCESS(200, "Operation successful"),
    CLIENT_ERROR(400, "Client error"),
    FAILURE(500, "Server failed");

    private final int code;
    private final String message;

    ResponseStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}

