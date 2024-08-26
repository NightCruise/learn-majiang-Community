package life.community.mapper;

import life.community.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Insert("INSERT INTO USER (account_id, name, login, token, gmt_create, gmt_modified, avatar_url) values (#{accountId},#{name},#{login},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
    void insert(User user);

    @Select("SELECT * FROM USER WHERE token = #{token}")
    User findByToken(String token);

    @Select("SELECT * FROM USER WHERE id = #{id}")
    User findById(@Param("id") Integer creator);

    @Select("SELECT * FROM USER WHERE account_id = #{accountId}")
    User findByAccountId(String accountId);

    @Update("UPDATE user SET name = #{name}, login = #{login}, token = #{token}, gmt_modified = #{gmtModified}, avatar_url = #{avatarUrl} WHERE account_id = #{accountId}")
    void update(User user);
}
