package test;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

public class Person {
	
	private String name;
	private String surname;
	private int age;
	private boolean experienced;
	private double someDouble;
	private float someFloat;
	private BigDecimal someBigDecimal;
	private Date someDate;
	private Timestamp someTimestamp;
	
	public Person() {
	}
	
	public Person(String name, String surname, int age, boolean experienced, double someDouble, float someFloat, BigDecimal someBigDecimal, Date someDate, Timestamp someTimestamp) {
		this.name = name;
		this.surname = surname;
		this.age = age;
		this.experienced = experienced;
		this.someDouble = someDouble;
		this.someFloat = someFloat;
		this.someBigDecimal = someBigDecimal;
		this.someDate = someDate;
		this.someTimestamp = someTimestamp;
	}

	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getSurname() {
		return surname;
	}



	public void setSurname(String surname) {
		this.surname = surname;
	}



	public int getAge() {
		return age;
	}



	public void setAge(int age) {
		this.age = age;
	}



	public boolean isExperienced() {
		return experienced;
	}



	public void setExperienced(boolean experienced) {
		this.experienced = experienced;
	}



	public double getSomeDouble() {
		return someDouble;
	}



	public void setSomeDouble(double someDouble) {
		this.someDouble = someDouble;
	}
	
	
	public float getSomeFloat() {
		return someFloat;
	}

	
	public void setSomeFloat(float someFloat) {
		this.someFloat = someFloat;
	}
	

	public BigDecimal getSomeBigDecimal() {
		return someBigDecimal;
	}
	

	public void setSomeBigDecimal(BigDecimal someBigDecimal) {
		this.someBigDecimal = someBigDecimal;
	}

	
	public Date getSomeDate() {
		return someDate;
	}
	

	public void setSomeDate(Date someDate) {
		this.someDate = someDate;
	}

	
	public Timestamp getSomeTimestamp() {
		return someTimestamp;
	}
	

	public void setSomeTimestamp(Timestamp someTimestamp) {
		this.someTimestamp = someTimestamp;
	}

	
	@Override
	public String toString() {
		return "Person [name=" + name + ", surname=" + surname + ", age=" + age + ", experienced=" + experienced
				+ ", someDouble=" + someDouble + ", someFloat=" + someFloat + ", someBigDecimal=" + someBigDecimal
				+ ", someDate=" + someDate + ", someTimestamp=" + someTimestamp + "]";
	}
}
