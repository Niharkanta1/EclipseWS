package PropFileDiff;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.log4j.Logger;

import PropFileEditor.PropFileEditorMain;

public class PropFileDiff {
	private static String firstFile, secondFile, outputFile;
	private static final Logger logger;
	static {
	        logger = Logger.getLogger(PropFileEditorMain.class);
	    }

	public static void main(String[] args) {
		firstFile ="C:\\SupplyWEB_Head\\resourceBundle\\text_en.properties";
		secondFile ="C:\\SupplyWEB_Head\\resourceBundle\\text_iw.properties";
		outputFile="C:\\JIRA\\newDifference\\difference_en_iw_17June.txt";
		findDifference();
	}
	
	public static void findDifference() {
		logger.info("reading From Main File..... ");
		
		BufferedReader br1 = null, br2 = null;
		PrintWriter pw = null;
		int lineNo=0, missingCount=0;
		try {
			br1 = new BufferedReader(new FileReader(firstFile));
			pw = new PrintWriter(new File(outputFile));
			String sCurrentLine="", target ="", sCurrentFilter="";
			while((sCurrentLine=br1.readLine()) != null){
				boolean flag = false; String mainKey=" ";
				int firstIndex = sCurrentLine.indexOf('=');
				if(firstIndex!= -1)
					mainKey = sCurrentLine.substring(0,firstIndex);
				else
					continue;
				
				if(mainKey.startsWith(" ") || mainKey.startsWith("#")){
					if(sCurrentLine.length()>0){
					continue;
					}
				}
				br2 = new BufferedReader(new FileReader(secondFile));
				while ((sCurrentFilter = br2.readLine()) != null) {
					String newKey = " ";
					int firstIndex2 = sCurrentFilter.indexOf('=');
					if(firstIndex2 != -1)
						newKey = sCurrentFilter.substring(0,firstIndex2);
					if(newKey.startsWith(" ") || newKey.startsWith("#")){
						continue;
					}
					if(newKey.trim().equals(mainKey.trim())){
						System.out.println(mainKey);
						flag = true;
					}
				}
				if(!flag){
					pw.write(sCurrentLine+"\n");
					pw.flush();
				}
			}
		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
		finally{
			try {
				br1.close();
				br2.close();
			} catch (IOException e) {
				logger.error("Error while closing readers");
				e.printStackTrace();
			}			
			pw.close();
			logger.info("task completed.");
		}
	}

}
