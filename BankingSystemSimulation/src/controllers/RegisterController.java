package controllers;

import exceptions.ServiceLayerException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
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

    private final BankService bankService = new BankService();
    private final GoToService go = new GoToService();

    @FXML
    public void handleRegister() {
        try {
            String fullName = txtFullName.getText().trim();
            String userName = txtUsername.getText().trim();
            String password = txtPassword.getText().trim();
            String mobileNumber = txtMobile.getText().trim();
            String address = txtAddress.getText().trim();

            if (fullName.isEmpty() || userName.isEmpty() || password.isEmpty() || mobileNumber.isEmpty() || address.isEmpty()) {
                lblMessage.setText("Please fill all fields");
                return;
            }
            if (fullName.length() < 3) {
                lblMessage.setText("Full name is too short");
                return;
            }
            if (userName.length() < 5) {
                lblMessage.setText("Username must be at least 5 characters");
                return;
            }
            if (password.length() < 6) {
                lblMessage.setText("Password must be at least 6 characters");
                return;
            }
            if (!mobileNumber.matches("\\d{10}")) {
                lblMessage.setText("Enter valid 10-digit mobile number");
                return;
            }
            User existingUser =bankService.getUserByUsername(userName);
            if (existingUser != null) {
                lblMessage.setText("Username already exists");
                return;
            }

            User user = new User(fullName,userName,password,mobileNumber,address
            );

            boolean registered =bankService.registerUser(user);

            if (registered) {
                lblMessage.setText("Registration successful");
                go.goToPage("/view/login.fxml","Login",rootPane);
            } else {
                lblMessage.setText("Registration failed");
            }

        } catch (ServiceLayerException e) {
            lblMessage.setText(e.getMessage());

        } catch (Exception e) {
            lblMessage.setText("Something went wrong");
            e.printStackTrace();
        }
    }

    @FXML
    public void goBackToLogin() {
        try {
            go.goToPage("/view/login.fxml","Login",rootPane);
        } catch (Exception e) {
            lblMessage.setText("Unable to open login page");
            e.printStackTrace();
        }
    }
}