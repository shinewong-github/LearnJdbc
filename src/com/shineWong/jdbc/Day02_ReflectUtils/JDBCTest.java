package com.shineWong.jdbc.Day02_ReflectUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;

public class JDBCTest {
	
	/**
	 * <p>结果集已耗尽 testGet
	 * <p>从数据库表取出字段的，并把字段命名为别名，是跟对象的属性字段对应，因为返回的是
	 * <p>String getColumnLabel 取到的是被定义的别名
	 */
	@Test
	public void testGet() {
//		String school,String examCard, String studentName, int grade,
//		 int type,int flowId,String identityCard
		String sql = "SELECT flowId,examCard,type,studentName,whichSchool school,identityCard,grade FROM CET4student WHERE flowId= ?";
		System.out.println(sql);
		GraStudent graStudent = get(GraStudent.class, sql, 2);
		System.out.println(graStudent);
	}

	/**
	 *<p><T> T 原理:
	 *<pre>1：resultSet获取结果集-->ResultSetMetaData操作结果集详细的字段和值(别名)-->存到Map集合中
	 *<pre>---->反射到对象属性--->于是该对象有了数据
	 * 方法抽取：通用的查询方法：可以根据传入的 SQL、Class 对象返回 SQL 对应的记录的对象.
	 * <p><strong>使用BeanUtils时，要导入依赖包commons-logging</strong></p>
	 * 
	 * @param clazz(Class<T>)
	 * @param sql(String)
	 * @param args(Object)
	 * @return 
	 * @see Map#entrySet()
	 */
	public <T> T get(Class<T> clazz, String sql, Object... args) {
		T entity = null;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Map<String, Object> values = null;//键值对集合

		try {
			connection = JdbcUtilsV2.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				preparedStatement.setObject(i + 1, args[i]);
			}
			rs = preparedStatement.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			values = new HashMap<>();
			// if (!resultSet.isBeforeFirst()) {
			// System.out.println("The cursor is not before firstLine");
			// }
			// else
			// {
			while (rs.next()) {
				for (int i = 0; i < rsmd.getColumnCount(); i++) { //Returns the number of columns in this ResultSet object.
					String columnLabel = rsmd.getColumnLabel(i + 1);// getColumnLabel获取别名，从1开始,如果没有别名，就获取列名
					Object columnValue = rs.getObject(i + 1);// 由列获取值，不再使用getString()
					System.out.println(columnLabel);//打印列的别名
					System.out.println(columnValue);//打印列的别名
					// System.out.println("测试");
					values.put(columnLabel, columnValue);//按键值对放到集合中
				}
			}

			if (values.size() > 0) {
				entity = clazz.newInstance();//对应的关系映射对象
				/*一个set集合中元素是一个键值对*/
				Set<Map.Entry<String, Object>> entryseSet=values.entrySet();  
//				  for (Map.Entry<String, String> entry:entryseSet) 
				for (Map.Entry<String, Object> entry : entryseSet) {//放到set集合中
					String fieldName = entry.getKey();//再从set集合中获取出来
					Object value = entry.getValue();
					BeanUtils.setProperty(entity, fieldName, value);
//					BeanUtils.set
					
					//放到javabean中
//	 school中山大学
//	 .....examCard123426
//	 .....studentNameTOM
//	 .....grade600
//	 .....identityCard441219964500
//	 .....type4
//	 .....flowId2.
//	 flowId,type,identityCard,examCard,studentName,whichSchool school,grade 
//					 System.out.println("测试"+entity+value);
				}
//				
			}

		}

		// }
		catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtilsV2.release(connection, rs, preparedStatement);

		}
		return entity;
	}

	/*-----------------------------------------------------------------------------------------*/

	/**
	 * 使用 PreparedStatement 将有效的解决 SQL 注入问题.
	 */
	@Test
	public void testSQLInjection2() {
		String flowid = "1 OR type = ";
		String type = " OR '1'='1";
		String sql = "SELECT * FROM CET4student WHERE flowid = ? AND type = ?";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet re = null;
		try {
			connection = JdbcUtilsV2.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, flowid);
			preparedStatement.setString(2, type);
			re = preparedStatement.executeQuery();
			if (re.next()) {
				System.out.println("登录成功!");
			} else {
				System.out.println("用户名和密码不匹配或用户名不存在. ");
				System.out.println("No data!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtilsV2.release(connection, null, preparedStatement);
		}
	}

/**
  * <p>测试 SQL 注入. or 1=1,可以对表执行操作包括删除，查找等 
  * <p>得出结论使用statement不安全。
  * <p>使用PreparedStatement安全
  */
	@Test
	public void testSQLInjection() {
		String sql = "SELECT * FROM CET4student WHERE flowid =' ' OR  type = ' ' OR '1'='1' ";
		System.out.println(sql);
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			connection = JdbcUtilsV2.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);

			if (resultSet.next()) {
				System.out.println("SQL注入成功 ！登录成功!");
			} else {
				System.out.println("用户名和密码不匹配或用户名不存在. ");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtilsV2.release(connection, resultSet, statement);
		}
	}

/**
 * 测试使用PreparedStatement对象来插入数据
 * PreparedStatement 发送sql语句时，操作字段可以是可变的参数，并用?代替
 */
	@Test
	public void testPreparedStatement() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = JdbcUtilsV2.getConnection();
			String sql = "INSERT INTO people (id, name, grade) VALUES(?,?,?)";
			preparedStatement = connection.prepareStatement(sql);
			// 可以改成for循环的形式，根据可变参数
			preparedStatement.setInt(1, 1);
			preparedStatement.setString(2, "黄光义");
			preparedStatement.setInt(3, 500);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtilsV2.release(connection, null, preparedStatement);
		}
	}

/**
 * <p>需求:
 * <p>从控制台读入一个整数, 确定要查询的类型:
 * <p>1. 用身份证查询. 2. 用准考证号查询 其他的无效. 并提示请用户重新输入.
 * @see #printStudent
 * @see #getSearchTypeFromConsole
 * @see #searchStudent
 */
	@Test
	public void testGetStudentBy1or2() {
		// 1. 得到查询的类型
		int searchType = getSearchTypeFromConsole();
		// 2. 具体查询学生信息
		Student student = searchStudent(searchType);
		// 3. 打印学生信息
		printStudent(student);
	}

/**
 * 打印学生信息: 若学生存在则打印其具体信息. 若不存在: 打印查无此人.
 * @param student
 */
	private void printStudent(Student student) {
		if (student != null) {
			System.out.println(student.toString());
		} else {
			System.out.println("查无此人!");
		}
	}

/**
 * 具体查询学生信息的. 返回一个 Student 对象. 若不存在, 则返回 null
 * @param searchType
 * @return
 */
	@SuppressWarnings("resource")
	private Student searchStudent(int searchType) {
		String sql = "SELECT * FROM CET4student WHERE ";
		Scanner scanner = new Scanner(System.in);
		if (searchType == 2) {
			System.out.print("请输入准考证号:");
			String examCard = scanner.next();
			sql = sql + "examcard = '" + examCard + "'";
		} else if(searchType == 1){
			System.out.println("请输入身份证号:");
			String identityCard = scanner.next();
			sql = sql + "identityCard = '" + identityCard + "'";
		}
		else{
			System.out.println("只能使用身份证号或者准考证号进行查询");
		}
		Student student = getStudent(sql);
		return student;
	}

/**
 * 根据传入的 SQL去获取结果集 并将从结果集中取出的字段值初始化给 Student 对象
 * @param sql
 * @return
 */
	private Student getStudent(String sql) {
		Student stu = null;
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			connection = JdbcUtilsV2.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				stu = new Student(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3),resultSet.getString(4), resultSet.getString(5), resultSet.getString(6), resultSet.getInt(7));
			}
//			                        flowId,studentName,TYPE,identityCard,examCard,whichSchool,grade
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtilsV2.release(connection, resultSet, statement);
		}
		return stu;
	}

	/**
	 * <p>需求:
	 * <p>从控制台读入一个整数, 确定要查询的类型:
	 * <p>1. 用身份证查询. 2. 用准考证号查询 其他的无效. 并提示请用户重新输入.
	 * 
	 * @return Integer(type:1/2)
	 */
	private int getSearchTypeFromConsole() {

		System.out.print("请输入查询类型String: 1. 用身份证查询. 2. 用准考证号查询 ");

		Scanner scanner = new Scanner(System.in);
		int type = scanner.nextInt();

		if (type != 1 && type != 2) {
			System.out.println("输入有误请重新输入!");
			throw new RuntimeException();
		}
		return type;
	}

	/**
	 * 测试内容: getStudentFromConsole() addNewStudent()或addNewStudent2()
	 */
	@Test
	public void addNewStudentTest() {
		Student student = getStudentFromConsole();
		addNewStudent2(student);
//		addNewStudent(student);
	}

	/**
	 * 通过Java-bean 从控制台输入的数据set为学生对象的值
	 */
	private Student getStudentFromConsole() {

		Scanner scanner = new Scanner(System.in);

		Student student = new Student();

		System.out.print("int FlowId:");
		student.setFlowId(scanner.nextInt());

		System.out.print("String StudentName:");
		student.setStudentName(scanner.next());
		
		System.out.print("int Type: ");
		student.setType(scanner.nextInt());

		System.out.print("String identityCard:");
		student.setIdCard(scanner.next());
		System.out.print("String ExamCard:");
		student.setExamCard(scanner.next());
		System.out.print("String WhichSchool:");
		student.setWhichSchool(scanner.next());
		System.out.print("int Grade:");
		student.setGrade(scanner.nextInt());
		
		scanner.close();
		return student;
	}

	/**
	 * <p>通过javabean get对象数据作为SQL语句的值插入到数据库中，使用PreparedStatement对象
	 * <p>addNewStudent2是addNewStudent的优化
	 * <p>String sql=可变的参数列表 --> values(?,?,?,?,?) ?代替了student.get()
	 * @param Student
	 * @return void
	 * @see #addNewStudent
	 * @see JdbcUtilsV2#update(String, Object...)
	 */
	public void addNewStudent2(Student student) {
		String sql = "INSERT INTO CET4student(flowid,studentname, type, identityCard, " + "examcard, whichSchool, grade) "
				+ "VALUES(?,?,?,?,?,?,?)";
		JdbcUtilsV2.update(sql, student.getFlowId(), student.getStudentName(),student.getType(), student.getIdentityCard(), student.getExamCard(),
				 student.getWhichSchool(), student.getGrade());
	}

/**
  * <P>通过javabean get对象数据作为SQL语句的值插入到数据库中，使用Statement对象
  * <P>values("")中的字段顺序要跟数据库表中的字段顺序一致
  * String sql=不是可变的参数列表，要拼凑VALUES(get字段名+,)比较麻烦，还可能会出错
  * @param Student 传入的javabean对象--也是数据库中的一张表
  * @return void
  * @see JdbcUtilsV2#update(String)
  */
	public void addNewStudent(Student student) {
		String sql = "INSERT INTO CET4student VALUES(" + student.getFlowId() + ",'" + student.getStudentName()+ "',"+ student.getType() + ",'"
				+ student.getIdentityCard() + "','" + student.getExamCard() + "','"
				+ student.getWhichSchool() + "'," + student.getGrade() + ");";
		System.out.println(sql);
//		flowId,studentName,TYPE,identityCard,examCard,whichSchool,grade
		JdbcUtilsV2.update(sql);
	}

}
