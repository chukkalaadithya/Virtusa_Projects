package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import models.Account;
import service.BankService;
import util.SessionManager;

public class DepositController {

    @FXML
    private Label lblAccountInfo;

    @FXML
    private Label lblBalance;

    @FXML
    private Label lblMessage;
    
    @FXML
    private BorderPane rootPane;

    @FXML
    private TextField txtAmount;

    private final BankService bankService = new BankService();

    private Account selectedAccount;

    @FXML
    public void initialize() {
        selectedAccount = SessionManager.getSelectedAccount();

        if (selectedAccount != null) {
        	lblAccountInfo.setText("Account: " +selectedAccount.getAccountType());

            lblBalance.setText("Available Balance: ₹" +String.format("%.2f",selectedAccount.getBalance()));
        }
    }

    @FXML
    public void depositAmount() {
        try {
            double amount = Double.parseDouble(txtAmount.getText().trim());

            if (amount <= 0) {
                lblMessage.setText("Enter valid amount");
                return;
            }

            double newBalance =selectedAccount.getBalance() + amount;

            boolean updated =bankService.updateBalance(selectedAccount.getAccountId(),newBalance);

            if (updated) {
                selectedAccount.setBalance(newBalance);

                SessionManager.setSelectedAccount(selectedAccount);

                lblMessage.setText("Deposit successful");

                lblBalance.setText("Available Balance: ₹" +String.format("%.2f", newBalance));

                txtAmount.clear();
                String str=bankService.saveTransaction(SessionManager.getSelectedAccountId(),"DEPOSIT",amount);
                System.out.println(str);
            } else {
                lblMessage.setText("Deposit failed");
            }

        } catch (Exception e) {
            lblMessage.setText("Enter valid amount");
            e.printStackTrace();
        }
    }

    @FXML
    public void goBack() {
    	service.GoBackService go=new service.GoBackService();
    	go.goBackService(rootPane);
    	
    }
}