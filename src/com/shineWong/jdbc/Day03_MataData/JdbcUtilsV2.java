package com.shineWong.jdbc.Day03_MataData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

//import com.shineWong.jdbc.day01_con.JDBCUtilsV1;

/**
 * <p>从类路径下的jdbc.properties读取链接数据库的信息，所有方法皆为静态。
 * <p>获取链接getconnection
 * <p>发送可变参数的PreparedStatement，执行数据库操作语句。
 * <p>释放链接release
 * @author ShineWong
 *@version JdbcUtilsV2
 */
public class JdbcUtilsV2 {
	private JdbcUtilsV2() {
	}

	private static String jdbcUrl = null;
	private static String username = null;
	private static String password = null;
	private static Properties properties = new Properties();

	/**
	 * <p>静态代码块，从配置文件中读取链接的信息
	 */
	static {
		try {
			properties.load(JdbcUtilsV2.class.getResourceAsStream("/jdbc.properties"));
		} catch (Exception e) {
			System.out.println("找不到文件");
		}
		jdbcUrl = (properties.getProperty("jdbcUrl"));
		username = (properties.getProperty("user"));
		password = (properties.getProperty("password"));
		try {
			Class.forName(properties.getProperty("MysqlDriver"));
		} catch (ClassNotFoundException e) {
			throw new ExceptionInInitializerError("加载驱动错误！");
		}
	}
	
/**
 * 新增结果集preparedStatement，逻辑安全。
 * <P>Object... args不确定的参数，可变的参数
 * @param sql
 * @param args
 */	
		public static void update(String sql, Object... args) {
			Connection connection = null;
			PreparedStatement preparedStatement = null;

			/*该方法在开发中常用*/
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
				JdbcUtilsV2.release(connection, null, preparedStatement);
			}
		}
		
/**
 * <p>statement.executeUpdate(sql)的封装
 * @param sql
 */
		public static void update(String sql) 
		{
			Connection connection = null;
			Statement statement = null;
			try
			{
				connection = getConnection();
				statement = connection.createStatement();
				statement.executeUpdate(sql);
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			} 
			finally 
			{
				release(connection, null, statement);
			}
		}

	/**
	 * 获取Connection链接
	 */
	public static Connection getConnection() throws Exception {
		return DriverManager.getConnection(jdbcUrl, username, password);
	}

	/**
	 * 释放能链接
	 * @param Connection
	 * @param ResultSet
	 * @param statement
	 * 
	 */
	public static void release(Connection connection, ResultSet rs, Statement statement) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (statement != null) {
					try {
						statement.close();
					} catch (SQLException e) {
						e.printStackTrace();
					} finally {
						if (connection != null) {
							try {
								connection.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}

		}

	}

	/**
	 * 释放能链接
	 * @param Connection
	 * @param statement
	 */
	public static void release(Statement statement, Connection conn) {

		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
/**
 * release
 * @param resultSet
 * @param statement
 * @param connection
 */
	public static void release(ResultSet resultSet, Statement statement,
			Connection connection) {

		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
