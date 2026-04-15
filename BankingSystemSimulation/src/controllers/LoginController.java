package controllers;

import exceptions.InvalidCredentialException;
import exceptions.ServiceLayerException;
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

    private final GoToService go = new GoToService();
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
            go.goToPage("/view/dashboard.fxml","Dashboard",rootPane);

        } catch (InvalidCredentialException e) {
            lblMessage.setText(e.getMessage());

        } catch (ServiceLayerException e) {
            lblMessage.setText(e.getMessage());

        } catch (Exception e) {
            lblMessage.setText("Login failed");
            e.printStackTrace();
        }
    }

    @FXML
    public void openRegister() {
        try {
            go.goToPage("/view/register.fxml","Register",rootPane);

        } catch (Exception e) {
            lblMessage.setText("Unable to open register page");
            e.printStackTrace();
        }
    }
}