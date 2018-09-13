package com.shineWong.jdbc.Day04_Dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

/**
 * 使用 QueryRunner 提供其具体的实现 比如现在数据库中有customer表 student表
 * Grastudent表，我们可能对这些表取一条记录(一条记录是一个对象)，也可能取多条记录(将多个对象封装到集合里面)
 * 也可能是一条记录的某个字段的值，也可能对数据库update的批量操作等，所以我们并不知到要操作什么表(对象)，所以引入了泛型<T>代表不确定的对象
 * 
 * @param <T>:
 *            子类需传入的泛型类型.
 * 
 */
public class JdbcDaoImpl<T> implements DAO<T> {

	private QueryRunner queryRunner = null;
	private Class<T> type;
	public JdbcDaoImpl() {
		queryRunner = new QueryRunner();
		type = ReflectUtils.getSuperGenericType(this.getClass());
		// this指的是实现该类的子类。
		// type是获取该类的T参数，也就是JdbcDaoImpl<T>的指定类型参数
	}
	/**
	 * 批量
	 * */
	@Override
	public void batch(Connection connection, String sql, Object[]... args) throws SQLException {
		queryRunner.batch(connection, sql, args);
	}

	/**
	 * 返回一条记录的第一个值
	 * */
	@Override
	public <E> E getForValue(Connection connection, String sql, Object... args) throws SQLException {
		return (E) queryRunner.query(connection, sql, new ScalarHandler(), args);
	}
	
	/**
	 * 返回多条记录吗，并封装成List
	 * */
	@Override
	public List<T> getForList(Connection connection, String sql, Object... args) throws SQLException {
		return queryRunner.query(connection, sql, new BeanListHandler<>(type), args);
	}

	/**
	 * 返回一条type对象记录,只是一条记录，没有多个type实例对象。
	 * */
	@Override
	public T get(Connection connection, String sql, Object... args) throws SQLException {
		return queryRunner.query(connection, sql, new BeanHandler<>(type), args);
	}
	
	/**
	 * update操作
	 * */
	@Override
	public void update(Connection connection, String sql, Object... args) throws SQLException {
		queryRunner.update(connection, sql, args);
		
	}
	
}
