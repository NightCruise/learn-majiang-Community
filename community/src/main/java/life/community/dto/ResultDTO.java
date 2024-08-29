package life.community.dto;

import life.community.exception.CustomizeErrorCode;
import life.community.exception.CustomizeException;
import lombok.Data;

@Data
public class ResultDTO {
    public ResultDTO(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    private Integer code;
    private String message;

    private static ResultDTO errorOf(Integer code, String message){
        return new ResultDTO(code, message);
    }

    public static ResultDTO errorOf(CustomizeErrorCode errorCode) {
        return ResultDTO.errorOf(errorCode.getCode(), errorCode.getMessage());
    }

    public static ResultDTO errorOf(CustomizeException ce) {
        return ResultDTO.errorOf(ce.getCode(), ce.getMessage());
    }

    public static ResultDTO okOf(){
        return new ResultDTO(200, "请求成功");
    }


}
