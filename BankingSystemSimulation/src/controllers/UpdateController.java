package controllers;

import exceptions.ServiceLayerException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import models.User;
import service.BankService;
import service.GoToService;
import util.SessionManager;

public class UpdateController {

	@FXML
	private BorderPane rootPane;

    @FXML
    private TextField txtFullName;

    @FXML
    private TextField txtMobile;

    @FXML
    private TextArea txtAddress;

    @FXML
    private Label lblMessage;

    private final BankService bankService =new BankService();
    private final GoToService go =new GoToService();

    @FXML
    public void initialize() {
        loadCurrentDetails();
    }

    private void loadCurrentDetails() {
        try {User user =SessionManager.getLoggedInUser();

            if (user == null) {
                lblMessage.setText("Session expired");
                return;
            }

            txtFullName.setText(user.getFullName());
            txtMobile.setText(user.getMobileNumber());
            txtAddress.setText(user.getAddress());

        } catch (Exception e) {
            lblMessage.setText("Unable to load details");
            e.printStackTrace();
        }
    }

    @FXML
    public void updateProfile() {
        try {
            String fullName =txtFullName.getText().trim();
            String mobile =txtMobile.getText().trim();
            String address =txtAddress.getText().trim();

            if (fullName.isEmpty() || mobile.isEmpty() || address.isEmpty()) {
                lblMessage.setText("Please fill all fields");
                return;
            }

            if (!mobile.matches("\\d{10}")) {
                lblMessage.setText("Enter valid 10-digit mobile number");
                return;
            }

            User user =SessionManager.getLoggedInUser();

            user.setFullName(fullName);
            user.setMobileNumber(mobile);
            user.setAddress(address);

            boolean updated =bankService.updateUserProfile(user);

            if (updated) {
                lblMessage.setText("Profile updated successfully");
            } else {
                lblMessage.setText("Update failed");
            }

        } catch (ServiceLayerException e) {
            lblMessage.setText(e.getMessage());

        } catch (Exception e) {
            lblMessage.setText("Unable to update profile");
            e.printStackTrace();
        }
    }

    @FXML
    public void goBack() {
        go.goToPage("/view/profile.fxml","Profile",rootPane);
    }
}