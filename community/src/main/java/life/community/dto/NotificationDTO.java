package life.community.dto;

import lombok.Data;

@Data
public class NotificationDTO {
    private Long id;
    private Long notifier;
    private Long gmtCreate;
    private Long outerId;
    private Integer status;
    private Integer type;
    private String typeName;
    private String notifierName;
    private String outerTitle;

}
