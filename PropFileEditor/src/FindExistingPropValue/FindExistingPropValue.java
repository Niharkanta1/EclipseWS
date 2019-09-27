package FindExistingPropValue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import PropFileEditor.PropFileEditorMain;

public class FindExistingPropValue {
	
	private static String mainFile,secondFile,outputFile;
	private static Set keySet = new HashSet();
	private static Map propMap = new HashMap();
	private static Properties myProps = new Properties();
	private static final Logger logger;
	static {
	        logger = Logger.getLogger(PropFileEditorMain.class);
	    }
	
	public static void main(String args[]){
		logger.info("Main : Started FindExistingPropValue.....");
		mainFile ="C:\\SupplyWEB_Head\\resourceBundle\\text_iw.properties"; // which has all values
		secondFile ="C:\\JIRA\\newDifference\\difference_v1.txt"; //missing values
		outputFile ="C:\\JIRA\\newFoundValues\\foundValues.txt";
		buildProps();
		findExistingValues();
		//createPropMap();
	}
	
	public static void buildProps(){
		Properties props = null;
		try {
		FileReader reader=new FileReader(mainFile);  
		props=new Properties();  
	    props.load(reader);
	    // get the properties and print
	    for(String key : props.stringPropertyNames()) {
	    	int index = key.indexOf(" ");
	    	if(index!= -1){
				String newkey = key.substring(index+1);
				myProps.put(newkey, props.getProperty(key));
	    	}
			else
				continue;
	    	}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		//myProps.list(System.out);
	  logger.info("End of Build Props");
	}
	
	public static void createPropMap() {
		logger.info("createPropMap : reading From Main File..... ");
		
		BufferedReader br1 = null;
		PrintWriter pw = null;
		int found=0, missingCount=0;
		try {
			br1 = new BufferedReader(new FileReader(mainFile));
			pw = new PrintWriter(new File(outputFile));
			String sCurrentLine="", target ="", sCurrentFilter="";
			while((sCurrentLine=br1.readLine()) != null){
				String mainKey="", key="";
				int firstIndex = sCurrentLine.indexOf('=');
				if(firstIndex!= -1)
					mainKey = sCurrentLine.substring(0,firstIndex);
				else
					continue;
				
				if(mainKey.startsWith(" ") || mainKey.startsWith("#")){
					//not a key
					if(sCurrentLine.length()>0){
					continue;
					}
				}
				else {
				//operation on key
					int firstIndex2 = sCurrentLine.indexOf('\\');
					if(firstIndex2!= -1){
						key = mainKey.substring(firstIndex2+1).trim();
						if(keySet.add(key)){
							propMap.put(key, sCurrentLine.substring(firstIndex+2).trim());
						}
					}
					else
						//no key value
						continue;
				}
			}
		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
		finally{
			try {
				br1.close();
			} catch (IOException e) {
				logger.error("Error while closing readers");
				e.printStackTrace();
			}			
			pw.close();
			logger.info("task completed.");
		}
	}
	
	public static void findExistingValues(){
		logger.info("createPropMap : reading From Main File..... ");
		
		BufferedReader br1 = null;
		PrintWriter pw = null;
		int found=0, missingCount=0;
		try {
			br1 = new BufferedReader(new FileReader(secondFile));
			OutputStream out = new FileOutputStream(new File(outputFile));
			pw = new PrintWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8), true);
			String sCurrentLine="", target ="", sCurrentFilter="";
			while((sCurrentLine=br1.readLine()) != null){
				String mainKey="", key="";
				int firstIndex = sCurrentLine.indexOf('=');
				if(firstIndex!= -1)
					mainKey = sCurrentLine.substring(0,firstIndex);
				else
					continue;
				
				if(mainKey.startsWith(" ") || mainKey.startsWith("#")){
					//not a key
					if(sCurrentLine.length()>0){
					continue;
					}
				}
				else {
				//operation on key
					int firstIndex2 = sCurrentLine.indexOf('\\');
					if(firstIndex2!= -1){
						key = mainKey.substring(firstIndex2+1).trim();
						if(myProps.containsKey(key)){
							found++;
							pw.write(mainKey.trim()+" = "+myProps.getProperty(key)+"\n");
							pw.flush();
						}
					}
					else
						//no key value
						continue;
				}
				
				}
			}
		catch (Exception e){
			e.printStackTrace();
		}
		finally{
			try {
				br1.close();
			} catch (IOException e) {
				logger.error("Error while closing readers");
				e.printStackTrace();
			}			
			pw.close();
			logger.info("task completed.");
		}
	}
}
