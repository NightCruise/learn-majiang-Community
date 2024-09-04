package life.community.enums;

public enum NotificationTypeEnum {

    COMMENT_QUESTION(1, "回复了问题"),
    REPLY_COMMENT(2, "回复了评论");

    private final int type;
    private final String name;

    NotificationTypeEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public static String nameOfType(int type) {
        for (NotificationTypeEnum typeEnum : NotificationTypeEnum.values()) {
            if (typeEnum.getType() == type){
                return typeEnum.getName();
            }
        }
        return "";
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }


}

