package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import models.Account;
import service.BankService;
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

    private final BankService bankservice = new BankService();

    private Account selectedAccount;
   
    
    @FXML
    public void initialize() {
        selectedAccount = SessionManager.getSelectedAccount();

        if (selectedAccount != null) {
            lblAccountInfo.setText( "Account: " + selectedAccount.getAccountType());

            lblBalance.setText("Available Balance: ₹" +String.format("%.2f",selectedAccount.getBalance()));
        }
    }

    @FXML
    public void withdrawAmount() {
        try {
            double amount = Double.parseDouble(txtAmount.getText().trim());

            if (amount <= 0) {
                lblMessage.setText("Enter valid amount");
                return;
            }

            if (amount > selectedAccount.getBalance()) {
                lblMessage.setText("Insufficient balance");
                return;
            }

            double newBalance =selectedAccount.getBalance() - amount;

            boolean updated =bankservice.updateBalance(selectedAccount.getAccountId(),newBalance);

            if (updated) {
                selectedAccount.setBalance(newBalance);

                SessionManager.setSelectedAccount(selectedAccount);

                lblMessage.setText("Withdrawal successful");

                lblBalance.setText("Available Balance: ₹" +String.format("%.2f", newBalance));

                txtAmount.clear();
                
                bankservice.saveTransaction(SessionManager.getSelectedAccountId(),"WITHDRAW",amount);
            } else {
                lblMessage.setText("Withdrawal failed");
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