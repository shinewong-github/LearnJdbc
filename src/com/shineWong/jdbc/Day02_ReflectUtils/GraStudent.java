package com.shineWong.jdbc.Day02_ReflectUtils;

/**
 * GraStudent javabean
 * @author ShineWong
 */
public class GraStudent {
	private int flowId;// 流水号
	private String studentName;// 学生名
	private int type;// 考试的类型
	private String identityCard;// 身份证号
	private String examCard;// 准考证号
	private String school;// 学生地址,是数据库字段whichSchool的别名 
	private int grade;// 考试分数.

	public int getFlowId() {
		return flowId;
	}

	public void setFlowId(int flowId) {
		this.flowId = flowId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getIdentityCard() {
		return identityCard;
	}

	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}

	public String getExamCard() {
		return examCard;
	}

	public void setExamCard(String examCard) {
		this.examCard = examCard;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

/**
 *<p> GraStudent构造函数,键值反射对应，参数的顺序要跟sqlq取出来的顺序一致
 *  <p>"SELECT flowId, type, identityCard identityCard, examCard examCard, studentName, "
				+ " whichSchool school, grade " + "FROM CET4student WHERE type = ?"</p>
 * @param flowId
 * @param type
 * @param idCard
 * @param examCard
 * @param studentName
 * @param whichSchool
 * @param grade
 */
	public GraStudent(String examCard, String studentName, int grade,
			 int type,int flowId,String identityCard,String school) {
		super();
		this.flowId = flowId;
		this.type = type;
		this.identityCard = identityCard;
		this.examCard = examCard;
		this.studentName = studentName;
		this.school = school;
		this.grade = grade;
	}
	
//	public GraStudent(int flowId, int type, String identityCard, String examCard,
//			 String studentName,String school,int grade) {
//		super();
//		this.flowId = flowId;
//		this.type = type;
//		this.identityCard = identityCard;
//		this.examCard = examCard;
//		this.studentName = studentName;
//		this.school = school;
//		this.grade = grade;
//	}
	

/**
 * 空构造函数
 */
	public GraStudent(){}
	@Override
	public String toString() {
		return "GraStudent [报名流水号=" + flowId + ", 考试类型=" + type + ", 身份证号="
				+ identityCard + ", e准考证号=" + examCard + ", 学生姓名="
				+ studentName + ", 所在学校=" + school + ", 分数=" + grade
				+ "]";
	}

}
