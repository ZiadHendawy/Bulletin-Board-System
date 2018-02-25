import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Parser{
	/**
	 * 
	 * @param path is the path of system.properties
	 * @return map of the system.properties fields by splitting 
	 * */
 	public  HashMap<String, String> parse(String path){
 		Path filePath = Paths.get("system.properties");
		Scanner scanner = null;
		
			 try {
				scanner = new Scanner(filePath);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		HashMap <String, String> map = new HashMap<String, String>();
		
		while(scanner.hasNextLine()){
			String scanned = scanner.nextLine();
			if(scanned.isEmpty())
				continue;
			String [] strArr = scanned.split("=");
			if(strArr.length != 0)
				map.put(strArr[0].trim(), strArr[1].trim());
			
		}
		
		//printing output map for testing purposes 
		for(Map.Entry<String, String> e : map.entrySet()){
			System.out.println(e.getKey()+":"+e.getValue());
		}
		return map;
 	}
	
	
}
