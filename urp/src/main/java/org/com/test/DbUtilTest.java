package org.com.test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.junit.Test;
import org.com.po.UrpUser;
import org.com.util.*;
public class DbUtilTest {
	QueryRunner queryRunner = new QueryRunner();
	@Test
	public void test1() throws Exception{
		Connection connection = JbdcUtil.getConnection();
		List<UrpUser> query = queryRunner.query(connection, "select * from urp_user", new BeanListHandler<>(UrpUser.class));
		System.out.println(query);
	}
}
