package controllers;

import java.util.List;

import exceptions.ServiceLayerException;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import models.Account;
import service.BankService;
import service.GoToService;
import util.SessionManager;

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
    private final GoToService go = new GoToService();

    @FXML
    public void initialize() {
        lblWelcome.setText("Welcome, " + SessionManager.getFullName());

        loadUserAccounts();
    }

    private void loadUserAccounts() {
        try {
            List<Account> accounts =bankService.getAccountsByUserId(SessionManager.getLoggedInUserId());

            cmbAccounts.getItems().clear();

            if (accounts == null || accounts.isEmpty()) {
                lblBalance.setText("No account found");
                lblMessage.setText("Please open a new account");
                return;
            }

            cmbAccounts.getItems().addAll(accounts);
            cmbAccounts.getSelectionModel().selectFirst();

            switchAccount();

        } catch (ServiceLayerException e) {
            lblMessage.setText(e.getMessage());

        } catch (Exception e) {
            lblMessage.setText("Unable to load accounts");
            e.printStackTrace();
        }
    }

    @FXML
    public void switchAccount() {
        try {
            Account selected =cmbAccounts.getValue();

            if (selected == null) {
                lblMessage.setText("Please select an account");
                return;
            }

            SessionManager.setSelectedAccount(selected);
            lblBalance.setText("Available Balance: ₹"+ String.format("%.2f",selected.getBalance()));
            lblMessage.setText(selected.getAccountType()+ " selected");

        } catch (Exception e) {
            lblMessage.setText("Error switching account");
            e.printStackTrace();
        }
    }

    @FXML
    public void openCreateAccountPage() {
        go.goToPage("/view/open_account.fxml","Open Account",lblBalance.getScene().getRoot());
    }

    @FXML
    public void openDepositPage() {
        go.goToPage("/view/deposit.fxml", "Deposit",lblBalance.getScene().getRoot());
    }

    @FXML
    public void openWithdrawPage() {
        go.goToPage("/view/withdraw.fxml","Withdraw",lblBalance.getScene().getRoot());
    }

    @FXML
    public void openTransferPage() {
        go.goToPage("/view/transfer.fxml","Transfer",lblBalance.getScene().getRoot());
    }

    @FXML
    public void openTransactions() {
        go.goToPage("/view/transactions.fxml","Transactions",lblBalance.getScene().getRoot());
    }

    @FXML
    public void viewProfile() {
        go.goToPage("/view/profile.fxml","Profile",lblBalance.getScene().getRoot());
    }

    @FXML
    public void updateProfile() {
        go.goToPage("/view/update_profile.fxml","Update Profile",lblBalance.getScene().getRoot());
    }

    @FXML
    public void logout() {
        SessionManager.logout();
        go.goToPage("/view/login.fxml","Login",lblBalance.getScene().getRoot());
    }
}