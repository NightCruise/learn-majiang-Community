package life.community.mapper;

import life.community.dto.QuestionDTO;
import life.community.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionMapper {

    @Insert("INSERT INTO QUESTION (title, description, gmt_create, gmt_modified, creator, tag) VALUES (#{title}, #{description}, #{gmtCreate}, #{gmtModified}, #{creator}, #{tag})")
    void creat(Question question);

    @Select("SELECT * FROM QUESTION LIMIT #{offset}, #{size}")
    List<Question> list(Integer offset, Integer size);

    @Select("SELECT COUNT(*) FROM QUESTION")
    Integer count();

    @Select("SELECT q.*, u.avatar_url FROM QUESTION q LEFT JOIN USER u ON q.creator = u.id")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "title", column = "title"),
            @Result(property = "description", column = "description"),
            @Result(property = "creator", column = "creator"),
            @Result(property = "gmtCreate", column = "gmt_create"),
            @Result(property = "gmtModified", column = "gmt_modified"),
            @Result(property = "commentCount", column = "comment_count"),
            @Result(property = "viewCount", column = "view_count"),
            @Result(property = "likeCount", column = "like_count"),
            @Result(property = "user.avatarUrl", column = "avatar_url")
    })
    List<QuestionDTO> listQuestions();

    @Select("SELECT * FROM QUESTION WHERE creator = #{userId}")
    List<QuestionDTO> listQuestionsByUser(Integer userId);

    @Select("SELECT * FROM QUESTION WHERE id = #{id}")
    Question findById(String id);

    @Update("UPDATE question SET title = #{title}, description = #{description}, gmt_modified = #{gmtModified}, tag = #{tag} WHERE id = #{id}")
    void update(Question question);
}
