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

public class TransferController {

    @FXML
    private TextField txtTargetAccount;

    @FXML
    private TextField txtAmount;

    @FXML
    private Label lblMessage;

    @FXML
    private BorderPane rootPane;

    private final BankService bankService = new BankService();
    private final GoToService go = new GoToService();

    @FXML
    public void transferMoney() {
        try {
            Account sourceAccount =SessionManager.getSelectedAccount();

            if (sourceAccount == null) {
                lblMessage.setText("Please select an account first");
                return;
            }

            String targetText =txtTargetAccount.getText().trim();

            String amountText =txtAmount.getText().trim();

            if (targetText.isEmpty() || amountText.isEmpty()) {

                lblMessage.setText("Please fill all fields");
                return;
            }

            int targetAccountId;
            double amount;

            try {
                targetAccountId =Integer.parseInt(targetText);

                amount =Double.parseDouble(amountText);

            } catch (NumberFormatException e) {
                lblMessage.setText("Enter valid details");
                return;
            }

            if (amount <= 0) {
                lblMessage.setText("Amount must be greater than zero");
                return;
            }

            if (targetAccountId== sourceAccount.getAccountId()) {

                lblMessage.setText("Source and target accounts cannot be same");
                return;
            }

            Account targetAccount =bankService.getAccountById(targetAccountId);

            if (targetAccount == null) {
                lblMessage.setText("Target account not found");
                return;
            }

            if (amount > sourceAccount.getBalance()) {
                lblMessage.setText("Insufficient balance");
                return;
            }

            double sourceNewBalance =sourceAccount.getBalance()- amount;
            double targetNewBalance =targetAccount.getBalance()+ amount;
            boolean sourceUpdated =bankService.updateBalance(sourceAccount.getAccountId(),sourceNewBalance);
            boolean targetUpdated =bankService.updateBalance(targetAccountId,targetNewBalance);
            if (!sourceUpdated || !targetUpdated) {
                lblMessage.setText("Transfer failed");
                return;
            }

            sourceAccount.setBalance(sourceNewBalance);
            SessionManager.setSelectedAccount(sourceAccount);
            bankService.saveTransaction(targetAccountId,"TRANSFER",amount);
            lblMessage.setText("Amount transferred successfully");
            txtTargetAccount.clear();
            txtAmount.clear();

        } catch (ServiceLayerException e) {
            lblMessage.setText(e.getMessage());

        } catch (Exception e) {
            lblMessage.setText("Unable to process transfer");
            e.printStackTrace();
        }
    }

    @FXML
    public void goBack() {
        go.goToPage("/view/dashboard.fxml","Dashboard",rootPane);
    }
}