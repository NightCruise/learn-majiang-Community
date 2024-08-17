package life.community.mapper;

import life.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    @Insert("INSERT INTO USER (account_id, name, login, token, gmt_create, gmt_modified) values (#{accountId},#{name},#{login},#{token},#{gmtCreate},#{gmtModified})")
    void insert(User user);

}
