package life.community.dto;

import life.community.exception.CustomizeErrorCode;
import life.community.exception.CustomizeException;
import lombok.Data;

@Data
public class ResultDTO<T> {
    private Integer code;
    private String message;
    private T data;

    private static ResultDTO<Void> errorOf(Integer code, String message){
        return new ResultDTO<>(code, message);
    }

    public static ResultDTO<Void> errorOf(CustomizeErrorCode errorCode) {
        return ResultDTO.errorOf(errorCode.getCode(), errorCode.getMessage());
    }

    public static ResultDTO<Void> errorOf(CustomizeException ce) {
        return ResultDTO.errorOf(ce.getCode(), ce.getMessage());
    }

    public static <T> ResultDTO<T> okOf(T t) {
        return new ResultDTO<>(200, "请求成功", t);
    }

    public static ResultDTO<Void> okOf(){
        return new ResultDTO<>(200, "请求成功");
    }


    public ResultDTO(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResultDTO(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
