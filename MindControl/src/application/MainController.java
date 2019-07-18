package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

public class MainController {
	
    @FXML
    private Button runButton;   
    @FXML
    private Label myMessage;    
    @FXML
    private CheckBox overlayCheckBox;
    
    private boolean buttonStatus = false; 

    @FXML
    protected void runAppliation(ActionEvent event) {
    	if(!buttonStatus) {	
    		myMessage.setText("Running..");
    		buttonStatus = true;
    		runButton.setText("Stop");
    	}
    	else {
    		buttonStatus = false;
    		runButton.setText("Run");
    	}
    }
    
    @FXML
    protected void startOverlay(ActionEvent event) {
    	if(overlayCheckBox.isSelected()) {
    		String msg = myMessage.getText();
    		myMessage.setText(msg+"\n"+"Overlay: on");
    	}
    	else {
    		myMessage.setText("Running..");
    	}
    }
}
