package ubersystem.Result;

import lombok.Data;

@Data
public class Result<T> {
    private String status;
    private String message;
    private T data;

    public Result() {
    }

    public Result(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
