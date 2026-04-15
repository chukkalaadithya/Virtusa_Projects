package service;

import javafx.fxml.FXMLLoader;
import exceptions.GoToServiceException;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GoToService {
	public void goToService(String path,String title,VBox rootPane) throws GoToServiceException {
		try {
	        Stage stage =(Stage) rootPane.getScene().getWindow();
	        FXMLLoader loader =new FXMLLoader(getClass().getResource(path));
	        stage.setScene(new Scene(loader.load()));
	        stage.setTitle(title);
	        stage.show();
	
	    } catch (Exception e) {
	    	e.printStackTrace();
	        throw new GoToServiceException("Error in Loading "+title+"page");
	    }
	}
	public void goToService(String path,String title,BorderPane rootPane) throws GoToServiceException {
		try {
	        Stage stage =(Stage) rootPane.getScene().getWindow();
	        FXMLLoader loader =new FXMLLoader(getClass().getResource(path));
	        stage.setScene(new Scene(loader.load()));
	        stage.setTitle(title);
	        stage.show();
	
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	throw new GoToServiceException("Error in Loading "+title+" page");
	    }
	}
	
}
