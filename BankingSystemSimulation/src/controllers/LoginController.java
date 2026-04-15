package controllers;

import exceptions.GoToServiceException;
import exceptions.InvalidCredentialException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import service.BankService;
import service.GoToService;

public class LoginController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label lblMessage;
    
    @FXML
    private VBox rootPane;

    private final GoToService go=new GoToService();
    private final BankService bankService = new BankService();

    @FXML
    public void handleLogin() {
        try {
            String username = txtUsername.getText().trim();
            String password = txtPassword.getText().trim();
            
            if (username.isEmpty() || password.isEmpty()) {
                lblMessage.setText("Please enter username and password");
                return;
            }
            	
            bankService.loginUser(username, password);
            go.goToService("/view/dashboard.fxml", "DashBoard", rootPane);
            
        }
        catch (InvalidCredentialException e) {
            lblMessage.setText(e.getMessage());
        }
        catch(GoToServiceException e) {
        	lblMessage.setText(e.getMessage());
        }
        catch(Exception e) {
        	lblMessage.setText("Login Failed !");
        	e.printStackTrace();
        }
    }

    @FXML
    public void openRegister() {
    	try {
	    	go.goToService("/view/register.fxml","Register",rootPane);
    	}
    	catch(Exception e) {
    		lblMessage.setText(e.getMessage());
    		e.printStackTrace();
    	}
    }

}