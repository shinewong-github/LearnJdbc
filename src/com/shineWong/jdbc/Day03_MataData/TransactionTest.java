package com.shineWong.jdbc.Day03_MataData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

/**
 * <p>测试事务的隔离测试
 * <p>事务的隔离级别 在 JDBC 程序中可以通过 Connection 的 setTransactionIsolation 来设置事务的隔离级别:
 *  <pre>Connection.setTransactionIsolation(有几个隔离级别参数)
 */
public class TransactionTest {


	@Test
	public void testTransactionIsolationUpdate() {
		
		Connection connection = null;

		try {
			connection = JdbcUtilsV2.getConnection();
			connection.setAutoCommit(false);//取消 Connection 的默认提交行为,默认是自动提交事务
			
			String sql = "UPDATE user SET balance = "
					+ "balance - 500 WHERE id = 1";
			update(connection, sql);
			
			connection.commit();//提交事务
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}
	
	@Test
	public void testTransactionIsolationRead() {
		String sql = "SELECT balance FROM user WHERE id = 1";
		Integer balance = getForValue(sql);
		System.out.println(balance); 
	}

/**
 *  返回某条记录的某一个字段的值 或 一个统计的值(一共有多少条记录等.)
 * @param sql
 * @param args
 * @return
 */
	public <E> E getForValue(String sql, Object... args) {

		// 1. 得到结果集: 该结果集应该只有一行, 且只有一列
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			// 1. 得到结果集
			connection = JdbcUtilsV2.getConnection();
			System.out.println("隔离级别："+connection.getTransactionIsolation()); 
			
			//读为未交
			connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			
           //读已提交
//			connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			
			preparedStatement = connection.prepareStatement(sql);

			for (int i = 0; i < args.length; i++) {
				preparedStatement.setObject(i + 1, args[i]);
			}

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return (E) resultSet.getObject(1);
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
	 * Tom 给 Jerry 汇款 500 元.
	 * 
	 * <p>关于事务: 
	 * <pre>1. 如果多个操作中每个操作使用的是自己的单独的连接, 则无法保证事务.
	 *  <pre>2. 具体步骤:
	 *       <pre>1). 事务操作开始前, 开始事务:取消 Connection 的默认提交行为. connection.setAutoCommit(false); 
	 *       <pre>2). 如果事务的操作都成功,则提交事务: connection.commit();
	 *       <pre>3). 回滚事务: 若出现异常, 则在 catch 块中回滚事务:
	 */
	@Test
	public void testTransaction() {

		Connection connection = null;

		try {

			connection = JdbcUtilsV2.getConnection();
			System.out.println(connection.getAutoCommit());

			// 开始事务: 取消默认提交.
			connection.setAutoCommit(false);

			String sql = "UPDATE user SET balance = "
					+ "balance - 500 WHERE id = 1";
			update(connection, sql);

			int i = 10 / 0;
			System.out.println(i);

			sql = "UPDATE users SET balance = " + "balance + 500 WHERE id = 2";
			update(connection, sql);

			// 提交事务
			connection.commit();
		} catch (Exception e) {
//			e.printStackTrace();
			System.out.println("异常则，回滚事务");

			// 提交事务前如果有异常则，回滚事务
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			JdbcUtilsV2.release(null, null, connection);
		}

	}

	public void update(Connection connection, String sql, Object... args) {
		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = connection.prepareStatement(sql);

			for (int i = 0; i < args.length; i++) {
				preparedStatement.setObject(i + 1, args[i]);
			}

			preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtilsV2.release(null, preparedStatement, null);
		}
	}

}
