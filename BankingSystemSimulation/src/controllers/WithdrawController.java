package controllers;

import exceptions.ServiceLayerException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import models.Account;
import service.BankService;
import service.GoToService;
import util.SessionManager;

public class WithdrawController {

    @FXML
    private Label lblAccountInfo;

    @FXML
    private Label lblBalance;

    @FXML
    private Label lblMessage;

    @FXML
    private TextField txtAmount;

    @FXML
    private BorderPane rootPane;

    private final BankService bankService = new BankService();
    private final GoToService go = new GoToService();

    private Account selectedAccount;

    @FXML
    public void initialize() {
        selectedAccount =SessionManager.getSelectedAccount();

        if (selectedAccount != null) {
            lblAccountInfo.setText("Account: "+ selectedAccount.getAccountType());

            lblBalance.setText("Available Balance: ₹"+ String.format("%.2f",selectedAccount.getBalance()));
        } else {
            lblMessage.setText("No account selected");
        }
    }

    @FXML
    public void withdrawAmount() {
        try {
            if (selectedAccount == null) {
                lblMessage.setText("Please select an account first");
                return;
            }

            String amountText =txtAmount.getText().trim();

            if (amountText.isEmpty()) {
                lblMessage.setText("Please enter amount");
                return;
            }

            double amount;

            try {
                amount = Double.parseDouble(amountText);
            } catch (NumberFormatException e) {
                lblMessage.setText("Enter valid amount");
                return;
            }

            if (amount <= 0) {
                lblMessage.setText("Amount must be greater than zero");
                return;
            }

            if (amount > selectedAccount.getBalance()) {
                lblMessage.setText("Insufficient balance" );
                return;
            }

            double newBalance =selectedAccount.getBalance()- amount;

            boolean updated =bankService.updateBalance(selectedAccount.getAccountId(),newBalance);

            if (!updated) {
                lblMessage.setText("Withdrawal failed");
                return;
            }

            selectedAccount.setBalance(newBalance);
            SessionManager.setSelectedAccount(selectedAccount);
            bankService.saveTransaction(selectedAccount.getAccountId(),"WITHDRAW",amount);
            lblBalance.setText("Available Balance: ₹"+ String.format("%.2f",newBalance));
            lblMessage.setText("Withdrawal successful");
            txtAmount.clear();

        } catch (ServiceLayerException e) {
            lblMessage.setText(e.getMessage());

        } catch (Exception e) {
            lblMessage.setText("Unable to process withdrawal");
            e.printStackTrace();
        }
    }

    @FXML
    public void goBack() {
        go.goToPage("/view/dashboard.fxml","Dashboard",rootPane);
    }
}