package life.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode{

    QUESTION_NOT_FOUND(2001, "你找的问题已经不存在了，请换个问题试一试吧"),
    TARGET_PARAM_NOT_FOUND(2002, "未选中任何问题和评论进行回复"),
    NO_LOGIN(2003, "当前操作需要登录，请登陆后重试"),
    SYS_ERROR(2004, "服务出错了，请稍后再试试吧！"),
    TYPE_PARAM_WRONG(2005, "评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006, "回复的评论不存在了，要不换一个试一试！"),
    CONTENT_IS_EMPTY(2007, "输入的内容不能为空！");


    private Integer code;
    private String message;


    CustomizeErrorCode(String message){
        this.message = message;
    }

    CustomizeErrorCode(Integer code, String message) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
