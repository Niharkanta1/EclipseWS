import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class IsAvailableInList {
	private static final String FILENAME = "C:\\JIRA\\SWB-2039\\WIP\\BranchCommitFiles.txt";
	private static final String BATCH = "C:\\JIRA\\SWB-2039\\WIP\\AllFiles.txt";
	public static void main(String[] args) {
		BufferedReader br = null, br2= null;
		try {

			br = new BufferedReader(new FileReader(FILENAME));
			String sCurrentLine, searchLine;
			int count =0, MissingCount=0;
			while ((sCurrentLine = br.readLine()) != null) {
				br2 = new BufferedReader(new FileReader(BATCH));
				boolean flag = false;
				while((searchLine = br2.readLine()) != null){
					if(sCurrentLine.trim().equals(searchLine.trim())){
						flag = true;
					}
				}
				if(flag){
					System.out.println(sCurrentLine);
					count++;
				}
				else{
					System.out.println(sCurrentLine + " -- Not present");
					MissingCount++;
				}
				
			}
			System.out.println("Count :"+count+" **** Missing Count :"+MissingCount);

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (br != null)
					br.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}
	}

}
