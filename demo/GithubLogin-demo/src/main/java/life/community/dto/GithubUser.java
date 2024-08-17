package life.community.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GithubUser {
    private String login;
    private String name;
    private Long id;
    private String bio;
}
