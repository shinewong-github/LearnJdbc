package com.shineWong.jdbc.Day04_Dao;

import java.sql.Connection;
import java.util.ArrayList;
import org.junit.Test;

public class CustomerDaoTest {
	CustomerDao customerDao = new CustomerDao();
	Connection connection = null;

/**
 *测试未通过
 */
	@Test
	public void testBatch() {
//		Connection connection=JdbcUtilsV4.getConnection();
//		String sql="insert into customer (id,name,email,birth) values(?,?,?,?)";
//		customerDao.batch(connection, sql, 1,"2","3","4");
		
	}

	@Test
	public void testGetForValue() {
		try {
			connection = JdbcUtilsV4.getConnection();
			String sql="select name from customer where id=?";
			String object=customerDao.getForValue(connection, sql, 7);
			System.out.println(object);

		}
		catch (Exception e) {
			// TODO: handle exception
		}
		finally {
			// TODO: finally it will do at last
		}
	}


	@Test
	public void testGetForList() {
		try {
			connection = JdbcUtilsV4.getConnection();
			String sql="select id,name,email,birth from customer where id in(?,?)";
			ArrayList<Customer> customers=(ArrayList<Customer>) customerDao.getForList(connection, sql, 1,7);
			System.out.println(customers);
			
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			JdbcUtilsV4.release(null, null, connection);

		}
	}

	/**
	 * 返回一条customer记录
	 */
	@Test
	public void testGet() {
		try {
			connection = JdbcUtilsV4.getConnection();
			String sql = "SELECT id, name AS customerName,email, birth FROM customer " + "WHERE id >?";
			Customer customer = customerDao.get(connection, sql, 6);
			System.out.println(customer);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtilsV4.release(null, null, connection);
		}
	}

/**
 * 测试Update
 */
	@Test
	public void testUpdate() {
		Connection connection = null;
		try {
			connection = JdbcUtilsV4.getConnection();
			String sql="delete from customer where id>?";
			customerDao.update(connection, sql, 7);
		}
		catch (Exception e) {
			e.printStackTrace();
		}finally{
			JdbcUtilsV4.release(null, null, connection);

		}

	}

}
