package csv_import;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CSVReader {
//	syso template => [CSVR]
	
	public static <T> List<T> readCSV(Class<T> objectClass, String pathToCSVFile){
//		System.out.println("[CSVR] objectClass Name: " + objectClass.getName());
//		System.out.println("[CSVR] objectClass Simple Name: " + objectClass.getSimpleName());
//		System.out.println("[CSVR] objectClass Package Name: " + objectClass.getPackageName());
//		System.out.println("[CSVR] objectClass Canonical Name: " + objectClass.getCanonicalName());
//		System.out.println("[CSVR] objectClass Type Name: " + objectClass.getTypeName());
//		String classString = objectClass.getClass().toString();
//		String qualifiedName = getQualifiedName(classString);
		
		
//		Checking if file is csv
		Optional<String> extension = getExtensionByStringHandling(pathToCSVFile);
		if (!extension.isPresent() || (extension.get().compareTo("csv") != 0)) {
			System.err.println("[CSVR] Extension of file " + pathToCSVFile + " is not .csv");
			return null;
		}
		
//		Getting file content via path assuming file is csv
		String fileContent = readFile(pathToCSVFile);
		System.out.println("[CSVR] File that was read: \n" + fileContent);
		if(fileContent.isEmpty()) {
			System.out.println("[CSVR] File is empty.");
			return null;
		}
		try {
//			Creating instance of Class<T>
			String qualifiedName = objectClass.getName();
			Class<?> c = Class.forName(qualifiedName);
			
//			Getting all (declared) fields of the Class<T>
			Field[] declaredFields = c.getDeclaredFields();
			System.out.println("[CSVR] Declared Fields: " + Arrays.toString(declaredFields));
			System.out.println("[CSVR] Declared Fields size: " + declaredFields.length);
			for (int i = 0; i < declaredFields.length; i++) {
				System.out.println("[CSVR] Declared Fields Type " + i + " => '" + declaredFields[i].getType().getSimpleName() + "'");
			}
			String[] rows = fileContent.split(System.lineSeparator());
//			System.out.println("[CSVR] First split results: \n" + Arrays.toString(rows));
			
//			Further splitting rows into columns
			List<String[]> rowsAndColumns = new ArrayList<String[]>();
			String[] column = null;
			for (int i = 0; i < rows.length; i++) {
				column = rows[i].split(",");
				if(declaredFields.length != column.length){
					System.err.println("[CSVR] Number of fields in Class '" + qualifiedName + "' does not match the number of columns in the given file at path '" + pathToCSVFile + "'.");
					return null;
				}
				rowsAndColumns.add(column);
			}
			
//			Creating an array of Constructors for said Class
			Constructor<?>[] constructors = c.getConstructors();
			Constructor<?> ctor = null;
			for (int i = 0; i < constructors.length; i++) {
				ctor = constructors[i];
				if(ctor.getGenericParameterTypes().length == 0) {
					break;
				}
//				else if(ctor.getGenericParameterTypes().length == declaredFields.length) {
//					break;
//				}
				else {
					ctor = null;
				}
			}
			if(ctor == null) {
//				case if there is not a zero parameter Constructor nor a Constructor with all fields as parameters
//				System.out.println("[CSVR] Constructor with no parameters nor with all parameters does not exist.");
				System.err.println("[CSVR] Constructor with no parameters not exist.");
				return null;
			}
			
//			Creating list of our future objects
			List<T> objectClassList = new ArrayList<>();
			Object tempObject = null;
			Class<?> currentFieldType = null;
			Object tempFieldValue = null;
//			String tempFieldName = null;
			for (int i = 0; i < rowsAndColumns.size(); i++) {
				try {
//					Logic for zero parameter Constructor
					if(ctor.getGenericParameterTypes().length == 0) {
						System.out.println("[CSVR] Parsing for 0 parameters..");
						tempObject = ctor.newInstance();
						System.out.println("[CSVR] Temp Object Type => " + tempObject.getClass().getSimpleName());
						for (int j = 0; j < declaredFields.length; j++) {
							currentFieldType = declaredFields[j].getType();
//							tempFieldName = declaredFields[j].getName();
//							System.out.println("[CSVR] Declared Field Name => " + declaredFields[j].getName());
//							System.out.println("[CSVR] Row: " + i + " , Column: " + j + " Field Type => '" + currentFieldType.getSimpleName() + "'");
							tempFieldValue = convertToSimpleType(rowsAndColumns.get(i)[j], currentFieldType.getSimpleName());
							if(tempFieldValue == null) {
								System.err.println("[CSVR] The value of the field at row " + i + " and column " + j + " does not match the declared field type for the beforementioned column position.");
								return null;
							}
							declaredFields[j].setAccessible(true);
							declaredFields[j].set(tempObject, tempFieldValue);
							declaredFields[j].setAccessible(false);
						}
						
						objectClassList.add((T) tempObject);
//					Logic for all fields as parameters Constructor
//					}else if(ctor.getGenericParameterTypes().length == declaredFields.length) {
//						System.out.println("[CSVR] Parsing for all " + declaredFields.length + " fields as parameters..");
//						
					}else {
//						Neither 0 parameters nor all parameters
//						System.err.println("[CSVR] The provided Class does not have a Constructor with neither 0 nor all fields as parameters. Can't parse.");
						System.err.println("[CSVR] The provided Class does not have a Constructor with 0 parameters. Can't parse.");
						return null;
					}
					
//					All of these exceptions should be handled smarter
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			return objectClassList;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
			
	}
	
	
//	Compare every type of fields we can get and return adequate value
	private static Object convertToSimpleType(String columnField, String type) {
		switch (type) {
		case "String":
			return columnField;
			
		case "int":
			int inty = Integer.parseInt(columnField);
			return inty;
		
		case "boolean":
			boolean boolly = Boolean.parseBoolean(columnField);
			return boolly;

		case "double":
			double doubly = Double.parseDouble(columnField);
			return doubly;
			
		case "float":
			float floaty = Float.parseFloat(columnField);
			return floaty;
		
		case "BigDecimal":
			BigDecimal bigDecimally = new BigDecimal(columnField);
			return bigDecimally;
		
		case "Date":
			Date datey;
			try {
				datey = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy").parse(columnField);
				return datey;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				System.err.println("Date was not able to parse the given Date in String format. Please check the format of the String that has been provided => " + columnField);
				e.printStackTrace();
				return null;
			}  
			
		case "Timestamp":
			Timestamp timestampy = Timestamp.valueOf(columnField);
			return timestampy;
		
		default:
			return null;
		}
		
	}
	
	
//	Method for returning the extension of a file
	private static Optional<String> getExtensionByStringHandling(String filename) {
		return Optional.ofNullable(filename).filter(f -> f.contains("."))
				.map(f -> f.substring(filename.lastIndexOf(".") + 1));
	}
	
	
//	Method for reading a file
	private static String readFile(String pathToFile) {
//		System.out.println("[CSVR] Started read file method: " + pathToFile);
		File users = new File(pathToFile);
		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader(users);
			br = new BufferedReader(fr);
			String fileOutput = br.lines().collect(Collectors.joining(System.lineSeparator()));
			System.out.println("[CSVR] Read file: " + pathToFile + " successfully!");
			return fileOutput;
		} catch (FileNotFoundException e) {
			System.err.println(
					"[CSVR] Error while reading file in path: " + pathToFile + " -> FileNotFoundException: " + e);
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (br != null)
					br.close();
				if (fr != null)
					fr.close();
			} catch (IOException e) {
				System.err.println("[CSVR] Error while closing readers on path: " + pathToFile + " -> IOException: " + e);
				e.printStackTrace();
			}
		}
	}
}
