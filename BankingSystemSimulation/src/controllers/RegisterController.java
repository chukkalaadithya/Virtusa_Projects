package controllers;

import dao.UserDAO;
import exceptions.InvalidCredentialException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.User;
import service.BankService;
import service.GoToService;

public class RegisterController {

    @FXML
    private TextField txtFullName;
    
    @FXML
    private VBox rootPane;

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtMobile;

    @FXML
    private TextArea txtAddress;

    @FXML
    private Label lblMessage;
    
    private final BankService bankService=new BankService();
    private final GoToService go = new GoToService();

    @FXML
    public void handleRegister() {  
        try {
            String fullName = txtFullName.getText().trim();
            String userName = txtUsername.getText().trim();
            String password = txtPassword.getText().trim();
            String mobileNumber = txtMobile.getText().trim();
            String address = txtAddress.getText().trim();

            if (fullName.isEmpty() || userName.isEmpty() || password.isEmpty() || mobileNumber.isEmpty() ||address.isEmpty()) {
                lblMessage.setText("Please fill all fields");
                return;
            }
            if(fullName.length()<=5) {
            	lblMessage.setText("Please enter Full Name, It is too short");
            	return;
            }
            if(userName.length()<=5) {
            	lblMessage.setText("UserName should be greater than 5 letters");
            	return;
            }
            
            User isUserNameExists=bankService.getUserByUsername(userName);
            if(isUserNameExists!=null) {
            	lblMessage.setText("UserName Exists! Choose different UserName or Please Login");
            	return;
            }
            
            User user = new User(fullName,userName,password,mobileNumber,address);
            boolean isRegistered = bankService.registerUser(user);

            if (isRegistered) {
                goBackToLogin();

            } else {
                lblMessage.setText("Registration failed");
            }

        }
        catch (Exception e) {
            lblMessage.setText("Something went wrong!");
            e.printStackTrace();
        }
    }
    
    @FXML
    public void goBackToLogin() {
    	try {
            go.goToService("/view/login.fxml", "Login",rootPane);

        } catch (Exception e) {
            lblMessage.setText(e.getMessage());
            e.printStackTrace();
        }
    }
}