package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import models.User;
import service.BankService;
import service.GoToService;
import util.SessionManager;

public class ProfileController {

    @FXML
    private Label lblFullName;

    @FXML
    private Label lblUsername;

    @FXML
    private Label lblMobile;

    @FXML
    private Label lblAddress;

    @FXML
    private Label lblMessage;

    @FXML
    private BorderPane rootPane;

    private final BankService bankService =new BankService();

    private final GoToService go =new GoToService();

    @FXML
    public void initialize() {
        loadUserProfile();
    }

    private void loadUserProfile() {
        try {
            String username =SessionManager.getUsername();

            if (username == null || username.isEmpty()) {
                return;
            }
            User user =bankService.getUserByUsername(username);

            if (user == null) {
                return;
            }
            lblFullName.setText("Full Name: " + user.getFullName());
            lblUsername.setText("Username: " + user.getUserName());
            lblMobile.setText("Mobile: " + user.getMobileNumber());
            lblAddress.setText("Address: " + user.getAddress());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void deleteProfile() {
        try {
            String username =SessionManager.getUsername();

            if (username == null || username.isEmpty()) {
                return;
            }

            User user =bankService.getUserByUsername(username);

            if (user == null) {
                return;
            }

            boolean deleted =bankService.deleteUser(user.getUserId());

            if (deleted) {
                SessionManager.logout();
                go.goToPage("/view/login.fxml","Login",rootPane);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goBack() {
        go.goToPage("/view/dashboard.fxml","Dashboard",rootPane);
    }
}