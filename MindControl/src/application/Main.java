package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
	        primaryStage.setTitle("MindControl Application");	       
			Scene scene = new Scene(root,300,200);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			scene.setFill(null);
			primaryStage.setScene(scene);
			primaryStage.show();
		} 
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	/*
	 * public void start(Stage stage) throws Exception { try { Label lbl = new
	 * Label("LABEL"); VBox p = new VBox(lbl);
	 * 
	 * //make the background of the label white and opaque
	 * lbl.setStyle("-fx-background-color: rgba(255, 255, 255, 1);");
	 * 
	 * //add some borders to visualise the element' locations lbl.setBorder(new
	 * Border(new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, null, null)));
	 * p.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID,
	 * null, null)));
	 * 
	 * Scene scene = new Scene(p); stage.setScene(scene);
	 * 
	 * //this is where the transparency is achieved: //the three layers must be made
	 * transparent //(i) make the VBox transparent (the 4th parameter is the alpha)
	 * p.setStyle("-fx-background-color: rgba(0, 0, 0, 0);"); //(ii) set the scene
	 * fill to transparent scene.setFill(null); //(iii) set the stage background to
	 * transparent stage.initStyle(TRANSPARENT);
	 * 
	 * stage.setWidth(200); stage.setHeight(100); stage.show(); } catch (Exception
	 * e) { e.printStackTrace(); } }
	 */
}
