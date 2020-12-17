package org.com.orm.strategy;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.com.util.OrmUtil;

public class ListBeanHandler<T> implements OrmHandler<List<T>> {
	private Class<T> clazz;

	public ListBeanHandler(Class<T> clazz) {
		// TODO Auto-generated constructor stub
		this.clazz = clazz;
	}

	public List<T> handler(ResultSet rs) {
		// TODO Auto-generated method stub
		// while生成bean
		ArrayList<T> arrayList = new ArrayList<>();
		if (rs == null) {
			return arrayList;
		}
		try {
			while (rs.next()) {
				T columnToProperty = OrmUtil.columnToProperty(rs, clazz);
				arrayList.add(columnToProperty);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return arrayList;
	}

}
