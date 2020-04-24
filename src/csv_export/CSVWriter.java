package csv_export;

import java.util.List;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CSVWriter {
//	syso template => [CSVW]
	
	public static <T> boolean writeCSV(List<T> listOfObjects, String pathToCSVFile) {
		Field[] fields;
		String writingString = "";
		Object o;
		T t;
		for (int i = 0; i < listOfObjects.size(); i++) {
			t = listOfObjects.get(i);
			fields = t.getClass().getDeclaredFields();
			for (int j = 0; j < fields.length; j++) {
				Field field = fields[j];
				try {
					field.setAccessible(true);
					o = runGetter(field, t);
//					System.out.println("[CSVW] field => " + field.getName() + ": " + o.toString());
					writingString +=o.toString();
					if(j != fields.length-1) {
						writingString+=",";
					}
					field.setAccessible(false);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(i != listOfObjects.size()-1) {
				writingString+=System.lineSeparator();
			}
		}
		System.out.println("[CSVW] Complete String => \n" + writingString);
		boolean finalResult = writeFile(pathToCSVFile, writingString);
		if(finalResult) {
			System.out.println("[CSVW] The list of classes has been successfully written into the csv file '" + pathToCSVFile + "'");
		}else {
			System.err.println("[CSVW] The program was unable to write the list of classes into the csv file.");
		}
		return finalResult;
	}
	
	
	private static <T> Object runGetter(Field field, T t){
		System.out.println("[RG_CSVW] Current field: " + field.getName());
		String prefix = "";
		int prefix_length = 0;
		if(field.getType().getSimpleName().compareTo("boolean")==0) {
			prefix = "is";
			prefix_length = 2;
		}else {
			prefix = "get";
			prefix_length = 3;
		}
	    for (Method method : t.getClass().getMethods()){
//	    	System.out.println("[RG_CSVW] Current method: " + method.getName());
	        if ((method.getName().startsWith(prefix)) && (method.getName().length() == (field.getName().length() + prefix_length))){
	            if (method.getName().toLowerCase().endsWith(field.getName().toLowerCase())){
	                try{
	                	System.out.println("[RG_CSVW] Found method called: " + method.getName());
	                    return method.invoke(t);
	                }
	                catch (IllegalAccessException e){
	                    System.err.println("[RG_CSVW] Could not determine method: " + method.getName());
	                }
	                catch (InvocationTargetException e){
	                	  System.err.println("[RG_CSVW] Could not determine method: " + method.getName());
	                }

	            }
	        }
	    }
	    System.err.println("[RG_CSV] Could not find method for field: " + field.getName() + ".");
	    return null;
	}
	
	
	private static boolean writeFile(String pathToCSVFile, String content) {
        File masterFile = new File(pathToCSVFile);
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter(masterFile);
            bw = new BufferedWriter(fw);
            bw.write(content);
//            System.out.println("File \'" + pathToCSVFile + "\' has been written into successfully.");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (bw != null)
                    bw.close();
                if (fw != null)
                    fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
