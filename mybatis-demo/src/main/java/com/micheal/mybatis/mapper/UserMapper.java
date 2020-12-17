package com.micheal.mybatis.mapper;

import com.micheal.mybatis.entity.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2020/12/15 17:55
 * @Description
 */
public interface UserMapper {
    @Select("select * from user where id = #{id} and name = #{name}")
    List<User> selectUserList(Integer id, String name);
}
