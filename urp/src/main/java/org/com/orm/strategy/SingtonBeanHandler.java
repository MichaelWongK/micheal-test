package org.com.orm.strategy;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.Date;

import org.com.util.OrmUtil;

public class SingtonBeanHandler<T> implements OrmHandler<T> {
	private Class<T> clazz;

	public SingtonBeanHandler(Class<T> clazz) {
		// TODO Auto-generated constructor stub
		this.clazz = clazz;
	}

	public T handler(ResultSet rs) {
		// TODO Auto-generated method stub]
		T newInstance = null;
		try {
			if (rs.next()) {
				newInstance = OrmUtil.columnToProperty(rs, clazz);
			}
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (T) newInstance;
	}

}
