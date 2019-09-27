import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class RemoveFilteredFilesFromMain {
	private static final String FILENAME = "C:\\Users\\ntripathy\\OneDrive - Infor\\Desktop\\WIP\\mainFile.txt";
	private static final String FILTERFILE = "C:\\Users\\ntripathy\\OneDrive - Infor\\Desktop\\WIP\\filterFile.txt";
	public static void main(String[] args) {
		BufferedReader br1 = null, br2 = null;
		FileReader fr = null;

		try {
			br1 = new BufferedReader(new FileReader(FILENAME));
			
			String sCurrentLine="";
			
				
				while ((sCurrentLine = br1.readLine()) != null) {
					boolean flag = true;
					br2 = new BufferedReader(new FileReader(FILTERFILE));
					String sCurrentFilter=br2.readLine();
					while(sCurrentFilter != null){
					if(sCurrentFilter.equals(sCurrentLine)){
						flag = false;
					}
					sCurrentFilter=br2.readLine();
				  }
					if(flag){
						System.out.println(sCurrentLine);
					}
				}
				
		} catch (IOException e) {

			e.printStackTrace();
			System.out.println("Exception occured.");
		} finally {

			try {

				if (br1 != null)
					br1.close();

				if (br2 != null)
					br2.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}
	}

}
