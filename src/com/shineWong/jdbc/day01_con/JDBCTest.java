package com.shineWong.jdbc.day01_con;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.junit.Test;

/**
 * 测试： 1：testDriver 
 * 2：testDriverManager
 * 3：getConnection
 * 4：testStatement
 * 5：testResultSet
 * 
 */
public class JDBCTest {

/**
 * <p>结果集测试resultSet
 */
	@Test
	public void testResultSet() {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			connection = JdbcUtilsV1.getConnection();
			statement = connection.createStatement();
			String selectAll = "SELECT * FROM CET4student";
			resultSet = statement.executeQuery(selectAll);
			
			/*用resultSet.next()获取每一个字段*/
			while (resultSet.next()) 
			{
				int flowID = resultSet.getInt(1);
				String studentName = resultSet.getString(2);
				String type = resultSet.getString(3);
				String identityCard = resultSet.getString(4);
				String examCard = resultSet.getString(5);
				String whichSchool = resultSet.getString(6);
				int grade = resultSet.getInt(7);
				System.out.println("ID号：" + flowID + " 学生姓名：" + studentName + " 考试类型：" + type + " 身份证号:" + identityCard
						+ " 准考证号:" + examCard + " 所在学校:" + whichSchool + " 分数:" + grade);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 6. 关闭数据库资源.
			JdbcUtilsV1.release(connection, resultSet, statement);
		}

	}


/**
 * 测试数据库操纵句法
 * <p>statement = conn.createStatement()
 * <p>statement:executeUpdate(sql)//包括insert into,update,delete...
 *    <p>executeQuery(sql)
 * <p>用户：hgy
 * <p>数据库：shinewong
 * <p>表:CET4student
 *  <p>表字段
 *   <p>flowId INT(4)：PRIMARY KEY 
     <p>studentName CHAR(50) 
     <p>type  INT(4) 
     <p>identityCard CHAR(50) 
     <p> examCard CHAR(6)
     <p>whichSchool CHAR(50) 
     <p>grade CHAR(50) 
 * 
 * @throws Exception
 */
		@Test
	public void testStatement() throws Exception {
			Connection conn = null;// 获取数据库连接
			Statement statement = null;//创建Statement对象
			try {
				conn = getConnection2();
				statement = conn.createStatement();
				
				/*query*/
//				String query = "select *from CET4student";
//				statement.executeQuery(query);
				
				/*INSERT INTO*/
//				String insertInto="INSERT INTO CET4student values (2,'黄光题',4,'441219964500','123426','中山大学',600)";
//				statement.executeUpdate(insertInto);
				
				/*DELETE*/
//				String deleteRecord = "DELETE FROM CET4student WHERE flowId =1";
//				  statement.executeUpdate(deleteRecord);
				
				/*UPDATE*/
//				 String updateValue = "UPDATE CET4student set studentName='TOM' where flowId = 2";
//				 statement.executeUpdate(updateValue);
				if(statement!=null)
				{
				 System.out.println( "测试Statement功能,成功！"+'\n'+statement);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					/*关闭 Statement 对象*/
					if (statement != null)//如果不为空，则创建了statement对象成功
						statement.close();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (conn != null)
						statement.close();//再次判断
					conn.close();//最后关闭连接
				}
			}
		}

/*--------------------------------------------------------------------*/
/**
 * 配置文件读取，并使用DriverManager.getConnection(jdbcUrl, user, password) 来测试连接
 * 
 * 
 * @return
 * @throws Exception
 */
		public Connection getConnection2() throws Exception {
			Properties properties = new Properties();
			InputStream in = this.getClass().getClassLoader().getResourceAsStream("jdbc.properties");

			properties.load(in);
			String user = properties.getProperty("user");
			String password = properties.getProperty("password");
			String jdbcUrl = properties.getProperty("jdbcUrl");
			String driver = properties.getProperty("MysqlDriver");
			Class.forName(driver);
			return DriverManager.getConnection(jdbcUrl, user, password);
		}
	/**
	 * 测试上面getConnection2方法
	 * @throws Exception
	 */
		@Test
		public void testGetConnection2() throws Exception {
			Connection connection=null;
			connection=getConnection2();
			if(connection!=null)
			{
				System.out.println("通过驱动管理数据库链接02测试成功！"+'\n'+connection);
			}
		}
/*---------------------------------------------------------------------------------*/	
/**
 *从配置文件(jdbc.properties)中读取数据库链接的信息
 *  过程： 从资源根目录下读取 数据连接信息的配置文件jdbc.properties-->通过加载到InputStream的方式，
 *   再用Properties的load(InputStream)来加载资源，
 *    然后使用getProperty("键")来读取值，从而把配置文件读到程序中。
 *  
 * @return
 * @throws Exception
 */
	public Connection getConnection() throws Exception {
			String driver = null;
			String jdbcUrl = null;
			String user = null;
			String password = null;
			Connection connection =null;
			
			InputStream in = getClass().getClassLoader().getResourceAsStream("jdbc.properties");
			Properties properties = new Properties();
			properties.load(in);
			
			driver = properties.getProperty("MysqlDriver");
			jdbcUrl = properties.getProperty("jdbcUrl");
			user = properties.getProperty("user");
			password = properties.getProperty("password");
			
			Properties info = new Properties();
			info.put("user", user);
			info.put("password", password);
			
	      connection = ((Driver) Class.forName(driver).newInstance()).
	    		      connect(jdbcUrl, info);
			return connection;
		}
		
/**
 * 测试上面getConnection方法
 * @throws Exception
 */
		@Test
		public void testGetConnection() throws Exception {
			Connection connection=null;
			   connection=getConnection();
			 if(connection!=null){
			   System.out.print("单个驱动的数据库链接01测试成功！"+'\n'+connection);
			}
		}
/*-----------------------------------------------------------------------------------*/	
/**
 * 测试DriverManager，管理所有不同厂商的驱动，是Driver的父接口
 * 
 * @throws Exception
 */
		@Test
		public void testDriverManager() throws Exception {
		
			String driver = "com.mysql.jdbc.Driver";
			String jdbcUrl = "jdbc:mysql://192.168.191.1:3306/shinewong";
			String user = "hgy";
			String password = "123456";
	// 2. 加载数据库驱动程序(对应的 Driver 实现类中有注册驱动的静态代码块.)
			Class.forName(driver);
			Connection connection = DriverManager.getConnection(jdbcUrl, user, password);
			
		  if(connection!=null)
			{
			  System.out.println("驱动管理测试成功");
			  System.out.println(connection);
			}
	}

/**
 * 测试数据库链接驱动
 * 业务介绍：
 *    数据库Driver是数据库厂商根据Java规范中的jdbc接口实现的。
 *    DriverManage管理各个数据库驱动。
 *    驱动链接成功就可以根据：
 *      driver=new com.mysql.jdbc.Driver()
 *      url=jdbc:mysql://ip:3306/databasename
 *      user=所要链接数据库的用户
 *      password=数据库密码
 * @throws SQLException
 */
	@Test
	public void testDriver() throws SQLException {
		Driver driver = new com.mysql.jdbc.Driver();
		String url = "jdbc:mysql://127.0.0.1:3306/shinewong";
		Properties info = new Properties();
		info.put("user", "hgy");
		info.put("password", "3421hgy1830");
		Connection connection = driver.connect(url, info);
		System.out.println(connection);
	}

}
