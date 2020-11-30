package com.micheal.demo.temp.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2020/11/30 22:36
 * @Description 已经实现了一个从数据库到对象的映射工具，那么从对象映射到数据库怎么玩
 * 在mybatis中：xxx.xml的映射配置文件
 * 在这个文件里配置了什么信息？
 * 1、配置了数据库里的ID 通过id标签
 * 2、配置了数据库列名 通过result标签
 * 3、在select update insert delete 标签内部已经默认生成了表名
 * 通过以上分析出：
 * 从对象映射到数据库需要标明：
 * 1、表名和对象的映射关系
 * 2、列名和属性的映射关系
 * 3、主键和属性的映射关系
 * 那么我们做ORM框架只需要解决以上问题。
 * 那么我们怎么解决？？？？
 * 在mybatis它从数据库--》对象的时候 自动生成了，那么如果我们没有使用它的逆向工程，那么我们得自己手写
 * xml配置文件。
 * 由此可知，表现出这样的映射关系有两种途径：
 * 1、配置文件
 * 2、注解
 *  <resultMap id="BaseResultMap" type="TRS.com.po.KmsEssay">
 * 		<id column="Essay_ID" jdbcType="INTEGER" property="essayId" />
 * 		<result column="Essay_Name" jdbcType="VARCHAR" property="essayName" />
 * 		<result column="Essay_Path" jdbcType="VARCHAR" property="essayPath" />
 * 		<result column="Essay_UploadMan" jdbcType="VARCHAR" property="essayUploadman" />
 * 		<result column="Essay_UploadTime" jdbcType="TIMESTAMP" property="essayUploadtime" />
 * 		<result column="Essay_ZipName" jdbcType="VARCHAR" property="essayZipname" />
 * 	</resultMap>
 */
public class JDBCDemo2 {

    public static void main(String[] args) throws Exception {
        MyGenerator myGenerator = new MyGenerator();
        myGenerator.init();
        Connection connection = myGenerator.getConnection();
        Statement createStatement = connection.createStatement();
        ResultSet resultSet = createStatement.executeQuery("select * from msg_log;");
        /*while (resultSet.next()) {
            System.out.println(resultSet.getString(2));
        }

        System.out.println("#####################");
        ResultSet resultSet2 = createStatement.getResultSet();
        resultSet2.beforeFirst();
        while (resultSet2.next()) {
            System.out.println(resultSet2.getString(2));
        }

        System.out.println(resultSet == resultSet2);
*/
        /*boolean execute = createStatement.execute("select * from msg_log;");
        System.out.println(execute);
        ResultSet resultSet2 = createStatement.getResultSet();
        while (resultSet2.next()) {
            System.out.println(resultSet2.getString(2));
        }*/

        boolean flag = createStatement.execute("create table xxx(xxx_id int primary key )");
        System.out.println(flag);
    }
}
