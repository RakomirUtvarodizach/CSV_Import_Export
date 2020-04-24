package test;

import java.math.BigDecimal;

public class Animal {
	private String name;
	private int age;

	private BigDecimal breedID;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BigDecimal getBreedID() {
		return breedID;
	}
	public void setBreedID(BigDecimal breedID) {
		this.breedID = breedID;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	@Override
	public String toString() {
		return "Animal [name=" + name + ", age=" + age + ", breedID=" + breedID + "]";
	}
	
	
	
	
	
}
