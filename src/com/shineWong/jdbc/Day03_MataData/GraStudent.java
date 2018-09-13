package com.shineWong.jdbc.Day03_MataData;

public class GraStudent {
	private int flowId;// 流水号
	private int type;// 考试的类型
	private String identityCard;// 身份证号
	private String examCard;	// 准考证号
	private String studentName;	// 学生名
	private String school;	// 学生地址
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

	public GraStudent(int flowId, int type, String identityCard, String examCard,
			String studentName, String school, int grade) {
//		super();
		this.flowId = flowId;
		this.type = type;
		this.identityCard = identityCard;
		this.examCard = examCard;
		this.studentName = studentName;
		this.school = school;
		this.grade = grade;
	}

	public GraStudent() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "您好研究生 "+"GraStudent [报名流水号=" + flowId + ", 考试类型=" + type + ", 身份证号="
				+ identityCard + ", 准考证号=" + examCard + ", 学生姓名="
				+ studentName + ", 所在学校=" + school + ", 分数=" + grade
				+ "]";
	}

	
}
