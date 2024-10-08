package life.community.mapper;

import life.community.dto.QuestionDTO;
import life.community.model.Question;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QuestionExtMapper {

    /**
     * 查询问题列表并携带用户头像
     */
    List<QuestionDTO> listQuestions(@Param("search") String search);

    /**
     *
     * @param userId
     * @return
     */
    List<QuestionDTO> listQuestionsByUser(Long userId);

    /**
     * 增加PV
     * @param record
     * @return
     */
    int incView(Question record);

    /**
     * 增加评论数
     * @param record
     * @return
     */
    int incCommentCount(Question record);

    /**
     * 推荐相关的问题
     * @param question
     * @return
     */
    List<Question> selectRelated(Question question);
}
