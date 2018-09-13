package com.shineWong.jdbc.Day04_Dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 *<p><strong> 开发常见的DAO借口定义</strong>>
 * 
 * <p>访问数据的 DAO 接口. 只是一个接口，还需要具体的实现类。
 * <p>可以是JDBC，可以是DBUtils技术(对Jdbc事项简单封装)等来实现。
 * 里边定义好访问数据表的各种方法
 * @param T: DAO 处理的实体类的类型, 一般对应数据库表类型对象。
 */
public interface DAO<T> {

	/**
	 * 定义量处理的方法
	 * @param connection
	 * @param sql
	 * @param args: 填充占位符的 Object [] 类型的可变参数.
	 * @throws SQLException 
	 */  
	void batch(Connection connection, 
			String sql, Object [] ... args) throws SQLException;
	
	/**
	 * 定义返回具体的一个值, 例如总人数, 平均工资, 某一个人的 email 等.
	 * @param connection
	 * @param sql
	 * @param args
	 * @return
	 * @throws SQLException 
	 */
	<E> E getForValue(Connection connection,
			String sql, Object ... args) throws SQLException;
	
	/**
	 * 定义返回 T 的一个集合
	 * @param connection
	 * @param sql
	 * @param args
	 * @return
	 * @throws SQLException 
	 */
	List<T> getForList(Connection connection,
			String sql, Object ... args) throws SQLException;
	
	/**
	 * 定义返回一个 T 的对象
	 * @param connection
	 * @param sql
	 * @param args
	 * @return
	 * @throws SQLException 
	 */
	T get(Connection connection, String sql, 
			Object ... args) throws SQLException;
	
	/**
	 * 定义INSRET, UPDATE, DELETE
	 * @param connection: 数据库连接
	 * @param sql: SQL 语句
	 * @param args: 填充占位符的可变参数.
	 * @throws SQLException 
	 */
	void update(Connection connection, String sql, 
			Object ... args) throws SQLException;

}
