package life.community.dto;

import lombok.Data;

@Data
public class GithubUser {
    private String login;
    private String name;
    private Long id;
    private String bio;
    private String avatarUrl;
}
