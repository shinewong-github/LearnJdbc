package com.shineWong.jdbc.Day03_MataData;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import org.junit.Test;

/**
 * readBlob
 * testInsertBlob
 * @author ShineWong
 *
 */
public class JDBCTest {

	/**
	 * <p>读取 blob 数据 的picture对象：Blob picture = resultSet.getBlob(5);
	 * <p>变成流InputStream in = picture.getBinaryStream()-->in.available-->out
	 * <p>1. 使用 getBlob 方法读取到 Blob 对象
	 * <p>2. 调用 Blob 的 getBinaryStream() 方法得到输入流。再使用 IO 操作即可. 
	 */
	@Test
	public void readBlob(){
		
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = JdbcUtilsV2.getConnection();
			String sql = "SELECT id, name customerName, email, birth, picture " 
					+ "FROM customer WHERE id = 20";
			preparedStatement = connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			
			//读取字段
			if(resultSet.next()){
				int id = resultSet.getInt(1);
				String name = resultSet.getString(2);
				String email = resultSet.getString(3);
				
				System.out.println(id + ", " + name  + ", " + email);
				Blob picture = resultSet.getBlob(5);
				
				InputStream in = picture.getBinaryStream();
				System.out.println(in.available()); 
				
				OutputStream out = new FileOutputStream("c:\\DateBase.jpg");
				
				byte [] buffer = new byte[1024];
				int len = 0;
				while((len = in.read(buffer)) != -1){
					out.write(buffer, 0, len);
				}
				
				in.close();
				out.close();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			JdbcUtilsV2.release(resultSet, preparedStatement, connection);
		}
	}
	
	/**
	 * <p>插入 BLOB 类型的数据必须使用 PreparedStatement：
	 *   <pre>因为 BLOB 类型 的数据时无法使用字符串拼写的。
	 * 调用 setBlob(int index, InputStream inputStream)
	 * <p>大小：BLOB 64k,MEDIUMBLOB 16M
	 * 
	 */
	@Test
	public void testInsertBlob(){
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try {
			connection = JdbcUtilsV2.getConnection();
			String sql = "INSERT INTO customer(id,name, email, birth, picture)" 
					+ "VALUES(?,?,?,?,?)";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, 20);
			preparedStatement.setString(2, "shinewong");
			preparedStatement.setString(3, "5442@sw.com");
			preparedStatement.setDate(4, 
					new Date(new java.util.Date().getTime()));
			//文件图片以读入流的方式来加载到程序内存中。
			InputStream inputStream = new FileInputStream("d:\\DataBaseTest.jpg");
			preparedStatement.setBlob(5, inputStream);
			
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			JdbcUtilsV2.release(null, preparedStatement, connection);
		}
	}
	
	/**
	 * <p>取得数据库自动生成的主键
	 * <p>preparedStatement.getGeneratedKeys()
	 */
	@Test
	public void testGetKeyValue() {
		
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try {
			connection = JdbcUtilsV2.getConnection();
			String sql = "INSERT INTO customer(name, email, birth)" +
					"VALUES(?,?,?)";
//			preparedStatement = connection.prepareStatement(sql);

			//使用重载的 prepareStatement(sql, flag) 
			//来生成 PreparedStatement 对象
			preparedStatement = connection.prepareStatement(sql, 
					Statement.RETURN_GENERATED_KEYS);
			
			preparedStatement.setString(1, "Liuwei");
			preparedStatement.setString(2, "Liuwei@yy.com");
			//日期类型
			preparedStatement.setDate(3, 
					new Date(new java.util.Date().getTime()));
			
			preparedStatement.executeUpdate();
			
			//通过 getGeneratedKeys() 获取包含了新生成的主键的 ResultSet 对象			
			//在 ResultSet 中只有一列 GENERATED_KEY, 用于存放新生成的主键值.
			ResultSet rs = preparedStatement.getGeneratedKeys();
			if(rs.next()){
				System.out.println(rs.getObject(1));
			}
			
			ResultSetMetaData rsmd = rs.getMetaData();
			for(int i = 0; i < rsmd.getColumnCount(); i++){
				System.out.println(rsmd.getColumnName(i + 1)); 
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			JdbcUtilsV2.release(null, preparedStatement, connection);
		}
		
	}

}
