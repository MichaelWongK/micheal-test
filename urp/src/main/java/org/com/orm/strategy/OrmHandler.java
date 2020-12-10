package org.com.orm.strategy;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 定义接口是为了抽象行为和属性
 * @author Smith
 *
 */
public interface OrmHandler<T> {
	T handler(ResultSet rs);
}
