package com.shineWong.jdbc.Day03_MataData;

import static org.junit.Assert.*;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import org.junit.Test;

/**
 *？：3个问题未解决
 *1：testGetForList()
 *2：testGetForValue()
 *3：testGetForValue()
 * 
 * */
public class DAOTest{
	
	DAO dao = new DAO();
	
/**
 * <p>插入操作：
 *  INSERT INTO customer(id,name,email, birth) VALUES(?,?,?,?)
 */
	@Test
	public void testUpdate() {
		String sql = "INSERT INTO customer(id,name,email, birth) VALUES(?,?,?,?)";
		dao.update(sql, 44,"XiaoMing", "xig@at.com", new Date(new java.util.Date().getTime()));
	}

/**
  *
  */
	@Test
	public void testGetForList() {
//		String sql = "SELECT flowId, type, examCard, "
//				+ "identityCard, studentName,whichSchool school, grade FROM cet4student";
			
		String sql = "SELECT id, name, email,birth FROM customer ";
		
//		List<GraStudent> graStudents= dao.getForList(GraStudent.class,sql);//传入学生对象
		List<Customer> customers= dao.getForList(Customer.class,sql);//传入学生对象
		
//		System.out.println("取出记录数为："+graStudents.size());
		System.out.println("取出记录数为："+customers.size());
		
//		System.out.println(graStudents);
		System.out.println(customers);
		
	}
	
	/**
	 *@?: Integer grade = dao.getForValue(sql):
	 * --java.lang.ClassCastException:
	 * ---- java.math.BigDecimal cannot be cast to java.lang.Integer
	 * */
	@Test
	public void testGetForValue() {
		String sql = null;
		// String sql = "SELECT studentName FROM CET4STUDENT WHERE flowid = ?";
		// String studentName = dao.getForValue(sql, 1);
		// System.out.println("studentName:"+studentName);

		sql = "SELECT max(examCard) FROM CET4student";
		String max_examCard = dao.getForValue(sql);
		
		sql = "SELECT min(examCard) FROM CET4student";
		String min_examCard = dao.getForValue(sql);

	}
	
	/**
	  *
	  * 
	  */
		@Test
		public void testGet()  {
			String sql = "SELECT  flowId , type, examCard, "
					+ "identityCard identity_Card , studentName , whichSchool school, grade FROM cet4student  WHERE flowId = ?";
			GraStudent graStudent = dao.get(GraStudent.class, sql, 4);
			System.out.println(graStudent);
		}
}

