package application;

import javafx.event.ActionEvent;

public class MainController {
	
	boolean aimBotButton = false;
	
	public void RunApp(ActionEvent event) {
		System.out.println("Application Is Running...");
	}
	
	public void RunAimbot(ActionEvent event) {
		aimBotButton = !aimBotButton;
		if(aimBotButton)
			System.out.println("Checked");
		else
			System.out.println("Unchecked");
	}
}
