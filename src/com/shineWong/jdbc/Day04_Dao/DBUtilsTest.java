package com.shineWong.jdbc.Day04_Dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryLoader;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.Test;

/**
 * <p>queryRunner.update(connection, sql, "MIKE", 7);
 * <p>Object obj = queryRunner.query(connection, sql, new MyResultSetHandler());
 * <p>queryRunner.query(conn, sql, new BeanListHandler<>(Customer.class));
 * <p>queryRunner.query(conn, sql, new BeanListHandler<>(Customer.class));
 * <p>ScalarHandler
 * <p>List<Map<String, Object>> mapList = queryRunner.query(connection, sql, new MapListHandler());
 * <p>为了简化开发，实际开发中，不实用原生的jdbc，
 *  而是使用其简单封装 DBUtils工具类。 
 *@author ShineWong
 *2017年9月18日 下午2:53:45
 */
public class DBUtilsTest {

	/**
	 * 创建一个内部类
	 */

	@SuppressWarnings("rawtypes")
	class MyResultSetHandler implements ResultSetHandler {

		/*---------handle方法返回一个List集合----------*/
		@Override
		public Object handle(ResultSet resultSet) throws SQLException {
			// System.out.println("handle....");
			// return "atguigu";

			List<Customer> customers = new ArrayList<>();
			while (resultSet.next()) {
				Integer id = resultSet.getInt(1);
				String name = resultSet.getString(2);
				String email = resultSet.getString(3);
				Date birth = resultSet.getDate(4);
				Customer customer = new Customer(id, name, email, birth);
				customers.add(customer);
			}
			return customers;
		}

	}

	/**
	 * <p>QueryLoader: 可以用来加载存放着 SQL 语句的资源文件. 
	 * <p>使用该类可以把 SQL 语句外置化到一个资源文件中. 以提供更好的解耦。
	 * @throws IOException
	 */
	@Test
	public void testQueryLoader() throws IOException {
		// / 代表类路径的根目录.
		Map<String, String> sqls = QueryLoader.instance().load("/sql.properties");
		String updateSql = sqls.get("UPDATE_CUSTOMER");
		System.out.println(updateSql);
	}

	/**
	 * <p>ScalarHandler: 可以返回指定列的一个值或返回一个统计函数的值.
	 * 把结果集转为一个数值(可以是任意基本数据类型和字符串， Date 等)返回
	 */
	@Test
	@SuppressWarnings("uncheck")
	public void testScalarHandler() {
		Connection connection = null;
		QueryRunner queryRunner = new QueryRunner();
//		String sql = "SELECT name, email FROM customer";
		String sql = "SELECT name FROM customer " + "WHERE id = ?";

		try {
			connection = JdbcUtilsV4.getConnection();
			Object count = queryRunner.query(connection, sql, new ScalarHandler(), 6);

			System.out.println(count);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtilsV4.release(null, null, connection);
		}
	}

	/**
	 *<p> MapListHandler: 把结果集转为一个 Map 对象的集合, 
	 *<p> 并返回. Map 的键:列名(而非列的别名), 值: 列的值
	 *<p>List<Map<String, Object>> mapList = queryRunner.query(connection, sql, new MapListHandler());
	 *返回符合结果集的多条记录,放在mapList中</p>
	 *
	 */
	@Test
	public void testMapListHandler() {
		Connection connection = null;
		QueryRunner queryRunner = new QueryRunner();

		String sql = "SELECT id, name, email, birth FROM customer";

		try {
			connection = JdbcUtilsV4.getConnection();
			List<Map<String, Object>> mapList = queryRunner.query(connection, sql, new MapListHandler());

			System.out.println(mapList);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtilsV4.release(null, null, connection);
		}
	}

	/**
	 * 4. MapHandler: 把结果集转为一个 Map 对象, 并返回. 若结果集中有多条记录, 仅返回 第一条记录对应的 Map 对象. Map
	 * 的键: 列名(而非列的别名), 值: 列的值
	 */
	@Test
	public void testMapHandler() {
		Connection connection = null;
		QueryRunner queryRunner = new QueryRunner();

		String sql = "SELECT id, name, email, birth " + "FROM customer WHERE id = ?";

		try {
			connection = JdbcUtilsV4.getConnection();
			Map<String, Object> map = queryRunner.query(connection, sql, new MapHandler(), 6);

			System.out.println(map);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtilsV4.release(null, null, connection);
		}
	}

	/**
	 * <p>queryRunner.query(conn, sql, new BeanListHandler<>(Customer.class));
	 * <p>测试 ResultSetHandler 的 BeanListHandler 实现类 BeanListHandler: 把结果集转为一个 Bean
	 * 的 List. 该 Bean 的类型在创建 BeanListHandler 对象时传入:
	 * new BeanListHandler<>(Customer.class)
	 * 
	 */

	@Test
	public void testBeanListHandler() {
		String sql = "SELECT id, name, email, birth FROM customer";

		// 1. 创建 QueryRunner 对象
		QueryRunner queryRunner = new QueryRunner();

		Connection conn = null;

		try {
			conn = JdbcUtilsV4.getConnection();

			Object object = queryRunner.query(conn, sql, new BeanListHandler<>(Customer.class));

			System.out.println(object);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtilsV4.release(null, null, conn);
		}
	}

	
	/*---------------------------------------下面是query和update--------------------------------------------------*/
	/**
	 * <p>Object obj = queryRunner.query(connection, sql, new MyResultSetHandler());
	 * QueryRunner 的 query 方法的返回值取决于 其 ResultSetHandler参数类的handle 方法的返回值
	 * testQuery From queryRunner.query
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void testQuery() {
		QueryRunner queryRunner = new QueryRunner();
		Connection connection = null;
		try {
			connection = JdbcUtilsV4.getConnection();//创建连接，c3p0。
			String sql = "SELECT id, name, email, birth FROM customer";
			Object obj = queryRunner.query(connection, sql, new MyResultSetHandler());
			// MyResultSetHandler对象已定义在当前类的内部类中
			System.out.println(obj);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtilsV4.release(null, null, connection);
		}
	}

	/**
	 * <p>测试queryRunner.update(connection, sql, "MIKE", 7);
	 * testUpdate:删除 修改 From queryRunner.update
	 */
	@Test
	public void testUpdate() {
		QueryRunner queryRunner = new QueryRunner();
		Connection connection = null;
		try {
			connection = JdbcUtilsV4.getConnection();
			// String sql = "DELETE FROM customer " + "WHERE id IN (?)";
			String sql = "UPDATE customer SET name = ? WHERE id = ?";
			queryRunner.update(connection, sql, "MIKE", 7);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtilsV4.release(null, null, connection);
		}
	}

}
