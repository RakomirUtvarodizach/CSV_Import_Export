package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import csv_export.CSVWriter;
import csv_import.CSVReader;
import test.Animal;
import test.Person;

public class Main {
//	syso template => [M]

	public static final String OUTPUT_PATH = "C:\\Users\\Anagnosti\\eclipse_diretories_2020\\eclipse-workspace\\CSV_Import_Export\\output\\";
	public static final String INPUT_PATH = "C:\\Users\\Anagnosti\\eclipse_diretories_2020\\eclipse-workspace\\CSV_Import_Export\\input\\";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		Testing CSVReader
//		Date date = new Date();
//		System.out.println(date.toString());
//		System.out.println("[M] Started main..");
		Scanner sc = new Scanner(System.in);
		boolean loop = true;
		boolean yesno = true;
		String outputFile = "";
		boolean result = false;
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println("Welcome to the testing part of the CSV Import Export app!\n"
				+ "Beneath you will find your choices along with a corresponding number.\n");
		while(loop) {
			System.out.println("Please write the number of the option you wish to proceed with: \n"
					+ "0 - Person\n"
					+ "1 - Animal\n"
					+ "Alternatively, you can write 'exit' to close the program.");
			String nl = sc.nextLine();
			try {
				int key = Integer.parseInt(nl);
				System.out.println("You have selected option No. " + key + ". Enjoy the results =>");
				switch (key) {
				case 0:
					List<Person> people = new ArrayList<Person>();
					System.out.println("[M] Reading people from file.. ");
					people = CSVReader.readCSV(Person.class, INPUT_PATH + "correct_test_input_person.csv");
					for (Person person : people) {
						System.out.println("[M] " + person.toString());
//						if(person.isExperienced()) {
//							System.out.println("[M] I have experience.");
//						}
					}
					
//					Writing part ->
					outputFile = OUTPUT_PATH + "people_output.csv";
					result = CSVWriter.writeCSV(people,outputFile);
					System.out.println("[M] CSVWriter result: " + result);
					break;
				
				case 1:
					List<Animal> animals = new ArrayList<Animal>();
					System.out.println("[M] Reading animals from file.. ");
					animals = CSVReader.readCSV(Animal.class, INPUT_PATH + "animals.csv");
					for (Animal animal : animals) {
						System.out.println("[M] " + animal.toString());
					}
					

//					Writing part ->
					outputFile = OUTPUT_PATH + "animals_output.csv";
					result = CSVWriter.writeCSV(animals,outputFile);
					System.out.println("[M] CSVWriter result: " + result);
					break;
					
				default:
					break;
				}

				yesno = true;
				String res="";
				System.out.println("\n*END_OF_TEST*");
				System.out.println("Thank you for using our testing example for the CSV Import Export software!");
				while(yesno) {
					System.out.println("Would you like to try again? Y/N");
					res = sc.nextLine();
					if(res.compareTo("Y")==0) {
						yesno = false;
						System.out.println("Booting the test again..");
					}else if(res.compareTo("N")==0) {
						yesno = false;
						loop = false;
						System.out.println("See you next time!");
					}else {
						System.out.println("Invalid input. Please try again.");
					}
				}
			} catch (NumberFormatException e) {
				// TODO: handle exception
				if(nl.compareTo("exit")==0) {
					loop = false;
					System.out.println("See you next time!");
				}else {
					System.out.println("Invalid input. Please try again.");
				}
			}
		}
		sc.close();
		System.out.println("[M] End of program.");
	}
	
	
	
	 public void deleteFile(String path, String name) throws FileNotFoundException{

	        boolean result = new File(path + "/" + name + ".txt").delete();
	        if (result) {
	            System.out.println("\tFile has been deleted successfully.");
	        } else {
	            System.out.println("\tFile deleting failed for some unknown reason.");
	        }
	    }

}
