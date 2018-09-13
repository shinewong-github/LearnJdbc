package com.shineWong.jdbc.Day03_MataData;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

/**
 * <p>DAO:Date Access Object数据访问对象。
 * <p>可以读取数据库中的数据，包装成对应的类。 可以读取一条记录，所有记录等等，自己封装。
 * <p>字段的类型是一种数据对象，多种数据对象，要放到集合中List等。
 */
public class DAO {

	/**
	 * <p>使用preparedStatement对象进行 update操作
	 * <p>@param sql(String)
	 * <p>@param args(Object) 
	 */
	public void update(String sql, Object... args) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = JdbcUtilsV2.getConnection();
			preparedStatement = connection.prepareStatement(sql);

			for (int i = 0; i < args.length; i++) {
				preparedStatement.setObject(i + 1, args[i]);
			}

			preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtilsV2.release(null, preparedStatement, connection);
		}
	}
	
	/**
	 * 返回某条记录的某一个字段的值 或 一个统计的值(一共有多少条记录等.)
	 * 
	 * @param sql(String)
	 * @param args(Object)
	 * @return <E> E
	 * @throws SQLException
	 */
	//
	public <E> E getForValue(String sql, Object... args) {

		// 1. 得到结果集: 该结果集应该只有一行, 且只有一列
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			// 1. 得到结果集
			connection = JdbcUtilsV2.getConnection();
			preparedStatement = connection.prepareStatement(sql);

			for (int i = 0; i < args.length; i++) {
				preparedStatement.setObject(i + 1, args[i]);
			}

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				E e = (E) resultSet.getObject(1);
				return e;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			JdbcUtilsV2.release(resultSet, preparedStatement, connection);
		}
		// 2. 取得结果

		return null;
	}

	/**
	 * 获取结果集的 ColumnLabel 将列的别名放入到List集合中
	 * 
	 * @param rs(ResultSet)
	 * @return List<String>
	 * @throws SQLException
	 */
	private List<String> getColumnLabels(ResultSet rs) throws SQLException {
		List<String> labels = new ArrayList<>();

		ResultSetMetaData rsmd = rs.getMetaData();
		for (int i = 0; i < rsmd.getColumnCount(); i++) {
			labels.add(rsmd.getColumnLabel(i + 1));
		}
		return labels;
	}

	/**
	 * 处理结果集-->放到Map(键值对的形式)集合中-->放到List集合中
	 * <p>返回多条记录，一条记录是一个map对象，一个map对象是一个键值对。
	 * @param resultSet(ResultSet)
	 * @return List<Map<String, Object>>
	 * @throws SQLException
	 */
	public List<Map<String, Object>> handleResultSetToMapList(ResultSet resultSet) throws SQLException {
		// 5. 准备一个 List<Map<String, Object>>:
		// 键: 存放列的别名, 值: 存放列的值. 其中一个 Map 对象对应着一条记录
		List<Map<String, Object>> values = new ArrayList<>();
		List<String> columnLabels = getColumnLabels(resultSet);
		// 7. 处理 ResultSet, 使用 while 循环
		while (resultSet.next()) {
			Map<String, Object> map = new HashMap<>();

			for (String columnLabel : columnLabels) {
				Object value = resultSet.getObject(columnLabel);//通过别名获取字段值
				map.put(columnLabel, value);//取出字段和值,放到map集合中。
			}
			// 11. 把一条记录的一个 Map 对象放入 5 准备的 List 中
			values.add(map);//返回多条记录，一条记录是一个map对象，一个map对象是一个键值对。
		}
		return values;
	}


	
	
	/**
	 * <p>获取表对象。
	 * <p>过程：取出List容器中的Map元素，并将键值对通过BeanUtils.setProperty(Object, propertyName,
	 * <p> propertyNalue)的 反射方式复制给对象，并将对象放入集合中
	 * 
	 * @param clazz(Class<T>)
	 * @param values
	 *            (List<Map<String, Object>> )
	 * @param
	 * @return <T> List<T>
	 */
	public <T> List<T> transfterMapListToBeanList(Class<T> clazz, List<Map<String, Object>> values)
			throws InstantiationException, IllegalAccessException, InvocationTargetException {
		List<T> result = new ArrayList<>();//存放Javabean对象
//		T bean = clazz.newInstance();
		if (values.size() > 0) {
			for (Map<String, Object> m : values) {
//				 bean = clazz.newInstance();
				T bean = clazz.newInstance();
				for (Map.Entry<String, Object> entry : m.entrySet()) {
					String propertyName = entry.getKey();
					Object value = entry.getValue();
					BeanUtils.setProperty(bean, propertyName, value);//从发送sql语句到把2结果集复制给bean
				}
				// 13. 把 Object 对象放入到 list 中.
				result.add(bean);//存放具有已经从数据库中获取到属性值的javabean对象。
			}
		}
		return result;
	}

	/**
	 * 整合：transfterMapListToBeanList()方法，handleResultSetToMapList()方法 传入 SQL 语句和
	 * Class 对象, 返回 SQL 语句查询到的记录对应的 Class 类的对象的集合
	 * 
	 * @param clazz: 对象的类型
	 * @param sql: SQL 语句
	 * @param args:填充 SQL 语句的占位符的可变参数.
	 * @return <T> List<T>
	 * @throws SQLException
	 */
	public <T> List<T> getForList(Class<T> clazz, String sql, Object... args)  {
		List<T> list = new ArrayList<>();
		Connection connection = null;
		//Statement st = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			// 1. 得到结果集
			connection = JdbcUtilsV2.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				preparedStatement.setObject(i + 1, args[i]);
			}
			resultSet = preparedStatement.executeQuery();
	/* 2. 处理结果集, 得到 Map 的 List, 其中一个 Map 对象, 就是一条记录. Map 的 key 为 reusltSet 中列的别名, Map 的 value为列的值.*/
			List<Map<String, Object>> values = handleResultSetToMapList(resultSet);//返回所有记录
			// 3. 把 Map 的 List 转为 clazz 对应的 List
			// 其中 Map 的 key 即为 clazz 对应的对象的 propertyName,
			// 而 Map 的 value 即为 clazz 对应的对象的 propertyValue
			list = transfterMapListToBeanList(clazz, values);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtilsV2.release(resultSet, preparedStatement, connection);
		}
		return list;//获取
		
	}


	/**
	 * 
	 * @param clazz(Class<T>)
	 * @param sql(String)
	 * @param args(Object)查询一条记录, 返回对应的对象
	 * @throws SQLException 
	 */

	public <T> T get(Class<T> clazz, String sql, Object... args) {
		List<T> result = getForList(clazz, sql, args);
		if (result.size() > 0) {
			return result.get(0);
		}

		return null;
	}
}
