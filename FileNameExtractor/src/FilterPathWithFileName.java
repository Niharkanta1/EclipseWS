import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FilterPathWithFileName {
	private static final String FILENAME = "C:\\Users\\ntripathy\\OneDrive - Infor\\Desktop\\WIP\\MovedToServer.txt";
	private static final String FILTERFILE = "C:\\Users\\ntripathy\\OneDrive - Infor\\Desktop\\WIP\\filter.txt";
	public static void main(String[] args) {
		BufferedReader br1 = null, br2 = null;
		FileReader fr = null;

		try {

			br2 = new BufferedReader(new FileReader(FILTERFILE));
			String sCurrentLine="", target ="", sCurrentFilter=br2.readLine();
			while(sCurrentFilter != null){
				br1 = new BufferedReader(new FileReader(FILENAME));
				while ((sCurrentLine = br1.readLine()) != null) {
					int lastIndex = sCurrentLine.lastIndexOf("\\");
					target = sCurrentLine.substring(lastIndex+1);
					if(target.equals(sCurrentFilter)){
						//System.out.println(sCurrentLine.substring(0,lastIndex)+" "+sCurrentLine.substring(lastIndex+1));
						System.out.println(sCurrentLine);
					}
				}
				sCurrentFilter=br2.readLine();
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
