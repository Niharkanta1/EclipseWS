package PropFileDiff;

import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import PropFileEditor.PropFileEditorMain;

public class PropFileDiffv2 {
	private static String firstFile, secondFile, outputFile;
	private static final Logger logger;
	static {
	        logger = Logger.getLogger(PropFileEditorMain.class);
	    }

	public static void main(String[] args) {
		firstFile ="C:\\SupplyWEB_Head\\resourceBundle\\text_en.properties";
		secondFile ="C:\\SupplyWEB_Head\\resourceBundle\\text_iw.properties";
		outputFile="C:\\JIRA\\newDifference\\difference.txt";
		findDifference();

	}

	private static void findDifference() {
	    Properties englishProps = new Properties();
	    /*englishProps.load(new FileInputStream(firstFile));
	    Properties iwprops = new Properties();
	    iwprops.load(new FileInputStream("installation.props"));*/
	    
		
	}

}
