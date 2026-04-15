package controllers;

import dao.UserDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import models.User;
import service.GoBackService;
import util.SessionManager;

public class ProfileController {

    @FXML
    private BorderPane rootPane;

    @FXML
    private Label lblFullName;

    @FXML
    private Label lblUsername;

    @FXML
    private Label lblMobile;

    @FXML
    private Label lblAddress;

    private final UserDAO userDAO = new UserDAO();

    @FXML
    public void initialize() {
        loadProfileDetails();
    }

    private void loadProfileDetails() {
        try {
            int userId =SessionManager.getLoggedInUserId();
            User user =userDAO.getUserById(userId);

            if (user != null) {
            	
                lblFullName.setText("Full Name: " +user.getFullName());
                lblUsername.setText("Username: " +user.getUserName());
                lblMobile.setText("Mobile Number: " +user.getMobileNumber());
                lblAddress.setText("Address: " +user.getAddress());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void deleteProfile() {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Profile");
            alert.setHeaderText("Confirm Delete");
            alert.setContentText("Are you sure you want to delete your profile?");

            ButtonType result =alert.showAndWait().orElse(ButtonType.CANCEL);

            if (result == ButtonType.OK) {
                int userId =SessionManager.getLoggedInUserId();
                boolean deleted =userDAO.deleteUser(userId);

                if (deleted) {              	
                    SessionManager.logout();
                    FXMLLoader loader =new FXMLLoader(getClass().getResource("/view/login.fxml"));
                    Stage stage =(Stage) rootPane.getScene().getWindow();
                    stage.setScene(new Scene(loader.load()));

                    stage.setTitle("Login");
                    stage.show();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goBack() {
        service.GoBackService go=new GoBackService();
        go.goBackService(rootPane);
    }
}