package ubersystem.Result;

public enum ResponseStatus {
    SUCCESS("200", "0","Operation successful"),
    CLIENT_ERROR("400", "1","Client error"),
    // user error
    CREATE_USER_ERROR("401", "2","Phone number, password or role cannot be null"),
    USER_ALREADY_EXIST("402", "3","User already exists"),

    AUTH_ERROR("403", "error code","User does not exist or password error"),

    FAILURE("500", "-1","Server failed");

    private final String code;
    private final String status;
    private final String message;

    ResponseStatus(String code, String status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getStatus() {
        return this.status;
    }
}

