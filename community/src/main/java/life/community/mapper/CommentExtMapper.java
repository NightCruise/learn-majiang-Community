package life.community.mapper;

import life.community.model.Comment;

public interface CommentExtMapper {

    /**
     * 增加评论数
     * @param record
     * @return
     */
    int incCommentCount(Comment record);
}
