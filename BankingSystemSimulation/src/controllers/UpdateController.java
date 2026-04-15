package controllers;

import java.awt.TextComponent;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

public class UpdateController {
	private TextComponent txtMobile;
	private TextComponent txtAddress;
	private TextComponent txtFullName;
	private BorderPane rootPane;
	
	
	@FXML
	public void enableNameEdit() {
	    txtFullName.setEditable(true);
	}

	@FXML
	public void enableMobileEdit() {
	    txtMobile.setEditable(true);
	}

	@FXML
	public void enableAddressEdit() { 
	    txtAddress.setEditable(true);
	}
	
	@FXML
	public void updateProfile() { 
		
	}
	
	@FXML
	private void goBack() {
		service.GoBackService go=new service.GoBackService();  
		go.goBackService(rootPane);
	}
}
