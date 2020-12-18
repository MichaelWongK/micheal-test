package com.micheal.demo.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2020/12/16 23:28
 * @Description
 */
public interface BlogMapper {

    Blog selectBlog(@Param("id") Integer id, @Param("name") String name);
}
