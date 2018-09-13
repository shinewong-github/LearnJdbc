package com.shineWong.jdbc.Day04_Dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import javax.sql.DataSource;
import org.junit.Test;


/**
 * 测试的内容：
 * 
 * 1：批量插入数据：testBatchWithStatement();
 * 2:批量插入数据：testBatchWithPreparedStatement;
 * 3: 积攒：testBatch 可以提高批量处理的效率
 * 4:测试数据源C3P0的链接和操作
 * */
public class JdbcTest {

	/**
	 *<p> 现在用数据源的方式来获取链接
	 *<p>通过dataSource = new ComboPooledDataSource("c3p0").getConnection()方式来链接
	 *  会自动找到.xml配置
	 */
	@Test
	public void testJdbcUtilsV4() throws Exception{
		Connection connection = JdbcUtilsV4.getConnection();
		System.out.println(connection); 
	}
	
	/**
	 * 1. 创建 c3p0-config.xml 文件, 
	 * 参考帮助文档中 Appendix B: Configuation Files 的内容
	 * 2. 创建 ComboPooledDataSource 实例；
	 * DataSource dataSource = 
	 *			new ComboPooledDataSource("helloc3p0");  
	 * 3. 从 DataSource 实例中获取数据库连接. 
	 */
	@Test
	public void testC3poWithConfigFile() throws Exception{
		DataSource dataSource = 
				new ComboPooledDataSource("c3p0"); //找到配置文件.xml里面的配置名字为helloc3p0的相关的 
		
		System.out.println(dataSource.getConnection()); 
		ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
//				(ComboPooledDataSource)// dataSource;
		System.out.println(comboPooledDataSource.getMinPoolSize()); 
	}
	

	/**
	 * dbcp数据源不用测试。用的是C3P0
	 * 
	 */
//	/**
//	 * 1. 加载 dbcp 的 properties 配置文件: 配置文件中的键需要来自 BasicDataSource
//	 * 的属性.
//	 * 2. 调用 BasicDataSourceFactory 的 createDataSource 方法创建 DataSource
//	 * 实例
//	 * 3. 从 DataSource 实例中获取数据库连接. 
//	 */
//	@Test
//	public void testDBCPWithDataSourceFactory() throws Exception{
//		
//		Properties properties = new Properties();
//		InputStream inStream = JDBCTest.class.getClassLoader()
//				.getResourceAsStream("dbcp.properties");
//		properties.load(inStream);
//		
//		DataSource dataSource = 
//				BasicDataSourceFactory.createDataSource(properties);
//		
//		System.out.println(dataSource.getConnection()); 
//		
////		BasicDataSource basicDataSource = 
////				(BasicDataSource) dataSource;
////		
////		System.out.println(basicDataSource.getMaxWait()); 
//	}
	
	
	
//	/**
//	 * 使用 DBCP 数据库连接池
//	 * 1. 加入 jar 包(2 个jar 包). 依赖于 Commons Pool
//	 * 2. 创建数据库连接池
//	 * 3. 为数据源实例指定必须的属性
//	 * 4. 从数据源中获取数据库连接
//	 * @throws SQLException 
//	 */
//	@Test
//	public void testDBCP() throws SQLException{
//		final BasicDataSource dataSource = new BasicDataSource();
//		
//		//2. 为数据源实例指定必须的属性
//		dataSource.setUsername("root");
//		dataSource.setPassword("1230");
//		dataSource.setUrl("jdbc:mysql:///atguigu");
//		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
//		
//		//3. 指定数据源的一些可选的属性.
//		//1). 指定数据库连接池中初始化连接数的个数
//		dataSource.setInitialSize(5);
//		
//		//2). 指定最大的连接数: 同一时刻可以同时向数据库申请的连接数
//		dataSource.setMaxActive(5);
//		
//		//3). 指定小连接数: 在数据库连接池中保存的最少的空闲连接的数量 
//		dataSource.setMinIdle(2);
//		
//		//4).等待数据库连接池分配连接的最长时间. 单位为毫秒. 超出该时间将抛出异常. 
//		dataSource.setMaxWait(1000 * 5);
//		
//		//4. 从数据源中获取数据库连接
//		Connection connection = dataSource.getConnection();
//		System.out.println(connection.getClass()); 
//		
//		connection = dataSource.getConnection();
//		System.out.println(connection.getClass()); 
//		
//		connection = dataSource.getConnection();
//		System.out.println(connection.getClass()); 
//		
//		connection = dataSource.getConnection();
//		System.out.println(connection.getClass()); 
//		
//		Connection connection2 = dataSource.getConnection();
//		System.out.println(">" + connection2.getClass()); 
//		
//		new Thread(){
//			public void run() {
//				Connection conn;
//				try {
//					conn = dataSource.getConnection();
//					System.out.println(conn.getClass()); 
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			};
//		}.start();
//		
//		try {
//			Thread.sleep(5500);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		
//		connection2.close();
//	}
//	
	
	/**
	 * <p>当 "积攒" 到一定程度, 就统一的执行一次.
	 * <p>且清空先前 "积攒" 的 SQL, 积累到300条插入记录时候，发送preparedStatement到数据库
	 * <p>preparedStatement.addBatch();积累SQL语句
	 * <p>preparedStatement.executeBatch()执行
	 */
	@Test
	public void testBatch(){
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String sql = null;
		try {
			connection = JdbcUtilsV4.getConnection();
			JdbcUtilsV4.beginTx(connection);//开始事务
			sql = "INSERT INTO customer VALUES(?,?,?,?,?)";
			preparedStatement = connection.prepareStatement(sql);
			Date date = new Date(new java.util.Date().getTime());
			long begin = System.currentTimeMillis();//单前时间
			for(int i = 0; i < 100000; i++){
				preparedStatement.setInt(1, i + 1);
				preparedStatement.setString(2, "name_" + i);
				preparedStatement.setString(3, "name@" + i+".com");
				preparedStatement.setDate(4, date);
				preparedStatement.setObject(5, "''");
				System.out.println("i="+i);
				//"积攒" SQL 
				preparedStatement.addBatch();
				
				//当 "积攒" 到一定程度, 就统一的执行一次. 并且清空先前 "积攒" 的 SQL
			   //积累到300条插入记录时候，发送preparedStatement到数据库
				if((i + 1) % 300 == 0){
					preparedStatement.executeBatch();//每300条执行一次。
					preparedStatement.clearBatch();//清除
				}
			}
			
			//若总条数不是批量数值的整数倍, 则还需要再额外的执行一次. 
			if(100000 % 300 != 0){
				preparedStatement.executeBatch();//执行零头
				preparedStatement.clearBatch();
			}
			
			long end = System.currentTimeMillis();
			
			System.out.println("Time: " + (end - begin)); //569
			
			JdbcUtilsV4.commit(connection);//提交事务
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("异常");
			JdbcUtilsV4.rollback(connection);
		} finally{
			JdbcUtilsV4.release(null, preparedStatement, connection);
		}
	}
	/**
	 * <p>使用preparedStatement.executeUpdate()，插入sql,语句。
	 * <p>一次插入一条sql语句，一共插入1000条。
	 * 如果中间有异常，则数据回滚。
	 * <p>
	 */

	@Test
	public void testBatchWithPreparedStatement(){
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String sql = null;
		
		try {
			connection = JdbcUtilsV4.getConnection();
			JdbcUtilsV4.beginTx(connection);
			sql = "INSERT INTO customer (id,name,email,birth) VALUES (?,?,?,?)";
			preparedStatement = connection.prepareStatement(sql);
			Date date = new Date(new java.util.Date().getTime());
			
			long begin = System.currentTimeMillis();
			//一次插入一条sql语句，一共插入1000条
			for(int i = 5; i < 10; i++){
				preparedStatement.setInt(1, i + 1);
				preparedStatement.setString(2, "name_" + i);
				preparedStatement.setString(3, "name_@" + i+".com");
				preparedStatement.setDate(4, date);
				preparedStatement.executeUpdate();
			}
			long end = System.currentTimeMillis();
			System.out.println("Time: " + (end - begin)); //9819
			
			JdbcUtilsV4.commit(connection);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("异常了");
			JdbcUtilsV4.rollback(connection);
		} finally{
			JdbcUtilsV4.release(null, preparedStatement, connection);
		}
	}
	
	/**
	 * <p>测试未通过
	 * <p>数据库链接成功，但是数据库没有反应。
	 * 
	 * <p>Statement的批量插入
	 * <p>使用Statement的缺点是要拼凑sql语句。
	 */
	@Test
	public void testBatchWithStatement(){
		Connection connection = null;
		Statement statement = null;
		String sql = null;
		try {
			connection = JdbcUtilsV4.getConnection();
			JdbcUtilsV4.beginTx(connection);
			statement = connection.createStatement();
			long begin = System.currentTimeMillis();
			for(int i = 5; i < 100; i++){
				sql = "INSERT INTO customer (id,name,email)VALUES (" + (i + 2) + ", 'name_" + i +"','54428@"+i + "')";
				System.out.println(sql);
				statement.executeUpdate(sql);
//				statement.addBatch(sql);
//				statement.executeBatch();
			}
			long end = System.currentTimeMillis();
			System.out.println("Time: " + (end - begin)); //39567
			JdbcUtilsV4.commit(connection);
		} catch (Exception e) {
			e.printStackTrace();
			JdbcUtilsV4.rollback(connection);
		} finally{
			JdbcUtilsV4.release(null, statement, connection);
		}
	}

}
