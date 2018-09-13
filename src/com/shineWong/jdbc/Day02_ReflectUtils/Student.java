package com.shineWong.jdbc.Day02_ReflectUtils;

public class Student {
//	flowId,studentName,TYPE,identityCard,examCard,whichSchool,grade
	private int flowId;// 流水号
    private String studentName;// 学生名
	private int type;// 考试的类型
	private String identityCard;// 身份证号
	private String examCard;// 准考证号
	private String whichSchool;// 学生地址
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

	public void setIdCard(String identityCard) {
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

	public String getWhichSchool() {
		return whichSchool;
	}

	public void setWhichSchool(String whichSchool) {
		this.whichSchool = whichSchool;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public Student(int flowId,String studentName, int type, String identityCard, String examCard,
			 String whichSchool, int grade) {
		super();
		this.flowId = flowId;
		this.type = type;
		this.identityCard = identityCard;
		this.examCard = examCard;
		this.studentName = studentName;
		this.whichSchool = whichSchool;
		this.grade = grade;
	}

/**
 * 放射用
 */
	public Student() {
	}

	@Override
	public String toString() {
		return "您好 "+"Student [报名流水号=" + flowId +", 学生姓名="
				+ studentName + ", 考试类型=" + type + ", 身份证号="
				+ identityCard + ", 准考证号=" + examCard + ", 所在学校=" + whichSchool + ", 分数=" + grade
				+ "]";
	}

	
}
