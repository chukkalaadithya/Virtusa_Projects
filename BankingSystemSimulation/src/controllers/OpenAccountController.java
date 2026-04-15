package controllers;

import exceptions.ServiceLayerException;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import models.Account;
import service.BankService;
import service.GoToService;
import util.SessionManager;

public class OpenAccountController {

    @FXML
    private ComboBox<String> cmbAccountType;

    @FXML
    private TextField txtInitialBalance;

    @FXML
    private Label lblMessage;

    @FXML
    private VBox rootPane;

    private final BankService bankService = new BankService();
    private final GoToService go = new GoToService();

    @FXML
    public void initialize() {
        cmbAccountType.getItems().clear();
        cmbAccountType.getItems().addAll("SAVINGS","CURRENT");
    }

    @FXML
    public void createAccount() {
        try {
            String accountType =cmbAccountType.getValue();
            String balanceText =txtInitialBalance.getText().trim();

            if (accountType == null) {
                lblMessage.setText("Please select account type");
                return;
            }

            if (balanceText.isEmpty()) {
                lblMessage.setText("Please enter initial balance");
                return;
            }
            double balance;

            try {
                balance = Double.parseDouble(balanceText);
            } catch (NumberFormatException e) {
                lblMessage.setText("Enter valid balance amount");
                return;
            }

            if (balance < 0) {
                lblMessage.setText("Balance cannot be negative");
                return;
            }

            Account existingAccount =bankService.getAccountByUserIdAccountType(SessionManager.getLoggedInUserId(),accountType);

            if (existingAccount != null) {
                lblMessage.setText("This account type already exists");
                return;
            }

            Account account = new Account();
            account.setUserId(SessionManager.getLoggedInUserId());
            account.setAccountType(accountType);
            account.setBalance(balance);

            boolean created =bankService.createAccount(account);

            if (!created) {
                lblMessage.setText("Account creation failed");
                return;
            }

            Account createdAccount =bankService.getAccountByUserIdAccountType(SessionManager.getLoggedInUserId(),accountType);
            SessionManager.setSelectedAccount(createdAccount);
            bankService.saveTransaction(createdAccount.getAccountId(),"DEPOSIT",balance);
            lblMessage.setText("Account created successfully");
            txtInitialBalance.clear();
            go.goToPage("/view/dashboard.fxml","Dashboard",rootPane);

        } catch (ServiceLayerException e) {
            lblMessage.setText(e.getMessage());

        } catch (Exception e) {
            lblMessage.setText("Unable to create account");
            e.printStackTrace();
        }
    }

    @FXML
    public void goBack() {
        go.goToPage("/view/dashboard.fxml","Dashboard",rootPane);
    }
}