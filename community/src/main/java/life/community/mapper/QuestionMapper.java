package life.community.mapper;

import life.community.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QuestionMapper {

    @Insert("INSERT INTO QUESTION (title, description, gmt_create, gmt_modified, creator, tag) VALUES (#{title}, #{description}, #{gmtCreate}, #{gmtModified}, #{creator}, #{tag})")
    void creat(Question question);

    @Select("SELECT * FROM QUESTION LIMIT #{offset}, #{size}")
    List<Question> list(Integer offset, Integer size);

    @Select("SELECT COUNT(*) FROM QUESTION")
    Integer count();
}
