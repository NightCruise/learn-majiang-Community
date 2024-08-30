package life.community.dto;

import lombok.Data;

@Data
public class CommentCreateDTO {
    private Long id;
    private String content;
    private Long parentId;
    private Integer type;
}
