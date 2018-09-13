package com.shineWong.jdbc.day01_con;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 *  jdcb链接工具：
 *  <p>使用静态代码块加载链接信息
 *  <p>释放链接的资源
 *  
 *  <p>从配置文件读取数据库信息来链接数据库、释放库链接.
 * @version JDBC:Version 1
 */
public final class JdbcUtilsV1
{
    private JdbcUtilsV1(){}
	private static String jdbcUrl = null; 
    private static String username = null; 
    private static String password = null; 
    private static Properties properties = new Properties(); 
/*设置为静态代码块，全局作用，先被加载：从配置文件获取链接数据库的信息*/
    static
    {
    	 try { 
         	properties.load(JdbcUtilsV1.class.getResourceAsStream("/jdbc.properties")); 
         } catch (Exception e) { 
                 System.out.println("文件不存在");
         } 
    	  jdbcUrl = (properties.getProperty("jdbcUrl")); 
          username = (properties.getProperty("user")); 
          password = (properties.getProperty("password")); 
        
          /*判断加载的驱动*/
       try
        {
        	Class.forName(properties.getProperty("MysqlDriver"));
        }
        catch (ClassNotFoundException e)
        {
            throw new ExceptionInInitializerError("加载驱动类出错！！");
        }
    }
    
/**
 * 创建Connection
 * @return
 * @throws Exception
 */
    public static Connection getConnection() throws Exception
    {
        return DriverManager.getConnection(jdbcUrl, username, password);
    }

/**
 * 释放资源
 * @param connection
 * @param resultSet
 * @param statement
 */
    public static void release(Connection connection, ResultSet resultSet, Statement statement)
    {
        if (resultSet != null)
        {
            try
            {
            	resultSet.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
            finally
            {
                if (statement != null)
                {
                    try
                    {
                        statement.close();
                    }
                    catch (SQLException e)
                    {
                        e.printStackTrace();
                    }
                    finally
                    {
                        if (connection != null)
                        {
                            try
                            {
                                connection.close();
                            }
                            catch (SQLException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

        }

    }
    
/**
 * 释放资源
 * @param statement
 * @param connection
 */
	public static void release(Statement statement, Connection connection) {
	      if (statement != null)
	        {
	           try{
	               statement.close();
	             }
	          catch (SQLException e){
	                e.printStackTrace();
	             }
	          finally{
	           if (connection != null) {
	              try {
	            	  connection.close();
	               }
	                  catch (SQLException e) {
	                       e.printStackTrace();
	                  }
	           }
	       }
	   }
  }

}
