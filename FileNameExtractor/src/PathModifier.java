import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class PathModifier {
	private static final String FILENAME = "C:\\Users\\ntripathy\\OneDrive - Infor\\Desktop\\WIP\\RevertFiles.txt";
	public static void main(String[] args) {
		BufferedReader br = null;
		FileReader fr = null;

		try {

			//br = new BufferedReader(new FileReader(FILENAME));
			fr = new FileReader(FILENAME);
			br = new BufferedReader(fr);

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				//int lastIndex = sCurrentLine.lastIndexOf("\\");
				System.out.println("C:\\SupplyWEB_Head\\"+sCurrentLine);
			}

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}
	}
}
