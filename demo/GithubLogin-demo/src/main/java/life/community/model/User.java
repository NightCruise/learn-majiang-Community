package life.community.model;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String accountId;
    private String name;
    private String login;
    private String token;
    private Long gmtCreate;
    private Long gmtModified;
}
