package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import models.Account;
import service.BankService;
import util.SessionManager;

import java.util.List;

import exceptions.ServiceLayerException;

public class DashboardController {

    @FXML
    private Label lblWelcome;

    @FXML
    private Label lblBalance;  

    @FXML
    private Label lblMessage;

    @FXML
    private ComboBox<Account> cmbAccounts;

    private final BankService bankService = new BankService();

    @FXML
    public void initialize() {
        lblWelcome.setText("Welcome, " +SessionManager.getFullName());
        loadUserAccounts();
    }

    private void loadUserAccounts() {
    	try {
    		
	        List<Account> accounts =bankService.getAccountsByUserId(SessionManager.getLoggedInUserId());
	        cmbAccounts.getItems().clear();
	        cmbAccounts.getItems().addAll(accounts);
	
	        if (!accounts.isEmpty()) {
	            cmbAccounts.getSelectionModel().selectFirst();
	            switchAccount();
	        } else {
	            lblBalance.setText("No account found");
	            lblMessage.setText("Please open a new account");
	        }
    	}
    	catch(Exception e) {
    		lblMessage.setText("Something Went Wrong");
    	}
    }

    @FXML
    public void switchAccount() {
    	try {
	        Account selected = cmbAccounts.getValue();
	
	        if (selected != null) {
	            SessionManager.setSelectedAccount(selected);
	            lblBalance.setText("Available Balance: ₹" +String.format("%.2f",selected.getBalance()));
	            lblMessage.setText(selected.getAccountType() + " selected");
	        }
    	}
    	catch(Exception e) {
    		lblMessage.setText("Error in switching bank Accounts");
    	}
    }

    @FXML
    public void openCreateAccountPage() {
        openPage("/view/open_account.fxml", "Open Account");
    }

    @FXML
    public void openDepositPage() {
        openPage("/view/deposit.fxml", "Deposit");
    }

    @FXML
    public void openWithdrawPage() {
        openPage("/view/withdraw.fxml", "Withdraw");
    }

    @FXML
    public void openTransferPage() {
        openPage("/view/transfer.fxml", "Transfer");
    }

    @FXML
    public void openTransactions() {
        openPage("/view/transactions.fxml", "Transactions");
    }

    @FXML
    public void viewProfile() {
    	openPage("/view/profile.fxml", "Profile");
    }

    @FXML
    public void updateProfile() {
    	openPage("/view/update_profile.fxml", "Update Profile Details");
    }

    @FXML
    public void logout() {
        SessionManager.logout();
        openPage("/view/login.fxml", "Login");
    }

    private void openPage(String path, String title) {
        try {
            Stage stage = (Stage) lblBalance.getScene().getWindow();
            FXMLLoader loader =new FXMLLoader(getClass().getResource(path));
            stage.setScene(new Scene(loader.load()));
            stage.setTitle(title);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}