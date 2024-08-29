package life.community.dto;

import lombok.Data;

@Data
public class CommentDTO {
    private Long id;
    private String content;
    private Long parent_id;
    private Integer type;
}
