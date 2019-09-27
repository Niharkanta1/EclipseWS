package PropFileEditor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

public class PropFileEditorMain {
	
	private static String mainFile, mainFile2, mergeFile, outputFile, duplicateFile, outputFile2, newEntries;
	private static final Logger logger;
	static {
	        logger = Logger.getLogger(PropFileEditorMain.class);
	    }
	public PropFileEditorMain(){
		
	}
	
	public static void main(String[] args) {
		
		mainFile = args[0]+".properties";
		mergeFile = args[1];
		mainFile2 = args[0]+"_filtered.tmp";
		outputFile = args[0]+"-2.txt.tmp";
		duplicateFile = args[0]+"-duplicates.tmp";
		outputFile2 = args[0]+"-2.properties";
		newEntries = args[0]+"-newEntries.tmp";
		File mainFileFiltered = removeDuplicates();
		//File mainFileFiltered = new File(mainFile2); 
		mergeFiles(mainFileFiltered);
	}
	private static File removeDuplicates() {
		logger.info("Removing Updated Entry From Main File..... ");
		File mainFileFiltered = new File(mainFile2);
		BufferedReader br1 = null, br2 = null;
		PrintWriter pw = null, pw2=null; 
		int lineNo=0, duplicateCount=0;
		try {
			br1 = new BufferedReader(new FileReader(mainFile));
			pw = new PrintWriter(mainFileFiltered);
			pw2 = new PrintWriter(new File(duplicateFile));
			String sCurrentLine="", target ="", sCurrentFilter="";
			while((sCurrentLine=br1.readLine()) != null){
				boolean flag = true; String mainKey=" ";
				int firstIndex = sCurrentLine.indexOf('=');
				if(firstIndex!= -1)
					mainKey = sCurrentLine.substring(0,firstIndex);
				if(mainKey.startsWith(" ") || mainKey.startsWith("#")){
					if(sCurrentLine.length()>0){
					pw.write(sCurrentLine+"\n");
					pw.flush();
					}
					continue;
				}
				br2 = new BufferedReader(new FileReader(mergeFile));
				while ((sCurrentFilter = br2.readLine()) != null) {
					String newKey = " ";
					int firstIndex2 = sCurrentFilter.indexOf('=');
					if(firstIndex2 != -1)
						newKey = sCurrentFilter.substring(0,firstIndex2);
					if(newKey.startsWith(" ") || newKey.startsWith("#")){
						continue;
					}
					if(newKey.trim().equals(mainKey.trim())){
						//System.out.println(sCurrentLine.substring(0,lastIndex)+" "+sCurrentLine.substring(lastIndex+1));
						//System.out.println("Duplicate found for line number :"+lineNo+ " and the Line :"+sCurrentLine);
						duplicateCount++;
						pw2.write(sCurrentLine+"\n");
						pw2.flush();
					    flag = false;
					}
				}
				lineNo++;
				if(flag){
					pw.write(sCurrentLine+"\n");
					pw.flush();
				}
			}
			logger.info("Duplicate Count: "+duplicateCount);
			logger.info("New File Generated -"+mainFile2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pw.write("EndOfTHeFile\\ FILE=END");
		pw.flush();
		return mainFileFiltered;
	}
	public static void mergeFiles(File mainFile2){
		logger.info("######## Starting MERGE ########\n");
		BufferedReader br = null;
		BufferedReader br2 = null;
		PrintWriter pw = null;
		int lineNo =0,  mCount=0;
		Set uniqueKeySet = new HashSet();
		try{
			br = new BufferedReader(new FileReader(mainFile2));
			pw = new PrintWriter(outputFile);
			String sCurrentLine;
			lineNo++;
			String lastKey = "";
			while ((sCurrentLine = br.readLine()) != null) {
				int lineNo2=0;
				int firstIndex = sCurrentLine.indexOf("\\");
				String newKey = sCurrentLine.substring(0,firstIndex+1);
				//System.out.println(newKey);
				if(newKey.startsWith(" ") || newKey.startsWith("#") || newKey.equals("")){
					if(sCurrentLine.length()>0){
					pw.write(sCurrentLine+"\n");
					pw.flush();
					}
					continue;
				}
				if(null!= lastKey && !newKey.equals(lastKey) && !uniqueKeySet.contains(lastKey)){
					//System.out.println("Insert in line :"+lineNo);
					br2 = new BufferedReader(new FileReader(mergeFile));
					String sCurrentLine2;
					lineNo2++;
					while ((sCurrentLine2 = br2.readLine()) != null) {
						int firstIndex2 = sCurrentLine2.indexOf("\\");
						String newKey2 = sCurrentLine2.substring(0,firstIndex2+1);
						if(lastKey.equals(newKey2)){
						//	System.out.println("jsp page end of line found: "+lineNo2);
							pw.write(sCurrentLine2+"\n");
							System.out.println(sCurrentLine2);
							mCount++;
							pw.flush();
						}
						lineNo2++;
					}
					pw.write(sCurrentLine+"\n");
					pw.flush();
					uniqueKeySet.add(lastKey); // completed LastKey
				}
				else{
					pw.write(sCurrentLine+"\n");
					pw.flush();
				}
				lineNo++;
				lastKey=newKey;
			}
			System.out.print("\n");
		}
		catch(Exception e){
			logger.error("Exception Occure in Merging!");
			e.printStackTrace();
		}
		finally{
			//System.out.println("Line number="+lineNo);
		}
		
		logger.info("######## Merging is successful! ########");
		logger.info("Total Merged Lines: "+mCount);
		addOtherLines();
	}
	private static void addOtherLines() {
		logger.info("Adding new Entry to Main File..... ");
		File mainOutputFile = new File(outputFile2);
		BufferedReader br1 = null, br2 = null, br3 = null;
		PrintWriter pw = null, pw2=null; 
		int lineNo=0, ExtraEntrycount=0;
		try {
			br1 = new BufferedReader(new FileReader(mergeFile)); 
			br3 = new BufferedReader(new FileReader(outputFile));
			pw =  new PrintWriter(mainOutputFile);
			pw2 = new PrintWriter(new File(newEntries));
			String sCurrentLine="", target ="", sCurrentFilter="";
			while((sCurrentLine=br3.readLine()) != null){
				if(!sCurrentLine.equals("EndOfTHeFile\\ FILE=END"))
				pw.write(sCurrentLine+"\n");
			}
			pw.flush();
			while((sCurrentLine=br1.readLine()) != null){
				boolean flag = false; String mainKey=" ";
				int firstIndex = sCurrentLine.indexOf('=');
				if(firstIndex!= -1)
					mainKey = sCurrentLine.substring(0,firstIndex);
				if(mainKey.startsWith(" ") || mainKey.startsWith("#")){
					if(sCurrentLine.length()>0){
					pw.write(sCurrentLine+"\n");
					pw.flush();
					}
					continue;
				}
				br2 = new BufferedReader(new FileReader(outputFile));
				while ((sCurrentFilter = br2.readLine()) != null) {
					String newKey = " ";
					int firstIndex2 = sCurrentFilter.indexOf('=');
					if(firstIndex2 != -1)
						newKey = sCurrentFilter.substring(0,firstIndex2);
					if(newKey.startsWith(" ") || newKey.startsWith("#")){
						continue;
					}
					if(newKey.trim().equals(mainKey.trim())){
						flag = true;
					}
				}
				lineNo++;
				if(!flag){
					ExtraEntrycount++;
					pw2.write(sCurrentLine+"\n");
					pw2.flush();
					pw.write(sCurrentLine+"\n");
					pw.flush();
				}
			}
			logger.info("New Entry Found : "+ExtraEntrycount);
			logger.info("Output File created! -"+outputFile2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	pw.close();
	pw2.close();
	}
}
