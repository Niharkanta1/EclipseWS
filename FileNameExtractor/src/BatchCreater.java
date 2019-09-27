import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class BatchCreater {
	private static final String FILENAME = "C:\\JIRA\\SWB-2039\\WIP\\AllFiles.txt";
	public static void main(String[] args) {
		BufferedReader br = null;

		try {

			br = new BufferedReader(new FileReader(FILENAME));

			String sCurrentLine;
			String xCopy ="echo f|xcopy /s /y ";
			String source = "C:\\SupplyWEB_Head\\";
			String destination = "C:\\CrimsonChanges\\";
			String batchConfirmCode = "@echo off\nsetlocal\n:PROMPT\nSET /P AREYOUSURE=Are you sure (Y/[N])?\nIF /I \"%AREYOUSURE%\" NEQ \"Y\" GOTO END\n",
				   batchConfirmCode2 = "\n:END\nendlocal\npause";
			
			System.out.println(batchConfirmCode);
			while ((sCurrentLine = br.readLine()) != null) {
				System.out.println(xCopy+source+sCurrentLine+" "+destination+sCurrentLine);
			}
			System.out.println(batchConfirmCode2);

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
