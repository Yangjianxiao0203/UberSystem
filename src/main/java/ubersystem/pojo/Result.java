package ubersystem.pojo;

import lombok.Data;

@Data
public class Result<T> {
    private int status;
    private String message;
    private T data;

    public Result() {
    }

    public Result(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
