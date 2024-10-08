package life.community.mapper;

import java.util.List;
import life.community.model.QuestionTag;
import life.community.model.QuestionTagExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface QuestionTagMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QUESTION_TAG
     *
     * @mbg.generated Sun Sep 15 13:36:21 GMT+08:00 2024
     */
    long countByExample(QuestionTagExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QUESTION_TAG
     *
     * @mbg.generated Sun Sep 15 13:36:21 GMT+08:00 2024
     */
    int deleteByExample(QuestionTagExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QUESTION_TAG
     *
     * @mbg.generated Sun Sep 15 13:36:21 GMT+08:00 2024
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QUESTION_TAG
     *
     * @mbg.generated Sun Sep 15 13:36:21 GMT+08:00 2024
     */
    int insert(QuestionTag record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QUESTION_TAG
     *
     * @mbg.generated Sun Sep 15 13:36:21 GMT+08:00 2024
     */
    int insertSelective(QuestionTag record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QUESTION_TAG
     *
     * @mbg.generated Sun Sep 15 13:36:21 GMT+08:00 2024
     */
    List<QuestionTag> selectByExampleWithRowbounds(QuestionTagExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QUESTION_TAG
     *
     * @mbg.generated Sun Sep 15 13:36:21 GMT+08:00 2024
     */
    List<QuestionTag> selectByExample(QuestionTagExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QUESTION_TAG
     *
     * @mbg.generated Sun Sep 15 13:36:21 GMT+08:00 2024
     */
    QuestionTag selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QUESTION_TAG
     *
     * @mbg.generated Sun Sep 15 13:36:21 GMT+08:00 2024
     */
    int updateByExampleSelective(@Param("record") QuestionTag record, @Param("example") QuestionTagExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QUESTION_TAG
     *
     * @mbg.generated Sun Sep 15 13:36:21 GMT+08:00 2024
     */
    int updateByExample(@Param("record") QuestionTag record, @Param("example") QuestionTagExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QUESTION_TAG
     *
     * @mbg.generated Sun Sep 15 13:36:21 GMT+08:00 2024
     */
    int updateByPrimaryKeySelective(QuestionTag record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QUESTION_TAG
     *
     * @mbg.generated Sun Sep 15 13:36:21 GMT+08:00 2024
     */
    int updateByPrimaryKey(QuestionTag record);
}