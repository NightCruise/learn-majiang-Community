package life.community.mapper;

import life.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Insert("INSERT INTO USER (account_id, name, login, token, gmt_create, gmt_modified, avatar_url) values (#{accountId},#{name},#{login},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
    void insert(User user);

    @Select("SELECT * from USER WHERE token = #{token}")
    User findByToken(String token);

    @Select("SELECT * from USER WHERE id = #{id}")
    User findById(@Param("id") Integer creator);
}
