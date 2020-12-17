package com.micheal.demo.temp.mybatis;

import com.alibaba.druid.pool.DruidDataSource;
import com.micheal.demo.mapper.Blog;
import com.micheal.demo.mapper.BlogMapper;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2020/12/16 23:21
 * @Description
 */
public class MybatisDemo {

    public static void main(String[] args) {

        DataSource dataSource = getDataSource();
        JdbcTransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("development", transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);
        configuration.addMapper(BlogMapper.class);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        BlogMapper blogMapper = sqlSession.getMapper(BlogMapper.class);

        System.out.println(blogMapper.selectBlog(1));


    }

    private static DataSource getDataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl("jdbc:mysql://micheal.wang:3306/test?useUnicode=true&characterEncoding=utf-8&serverTimezone=Hongkong&useSSL=false");
        druidDataSource.setUsername("root");
        druidDataSource.setPassword("mingkai13");
        return druidDataSource;
    }
}
