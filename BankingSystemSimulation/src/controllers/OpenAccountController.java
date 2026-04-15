package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Account;
import service.BankService;
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

    @FXML
    public void initialize() {
        cmbAccountType.getItems().addAll("SAVINGS", "CURRENT");
    }

    @FXML
    public void createAccount() {
        try {
            Account account = new Account();
            account.setUserId(SessionManager.getLoggedInUserId());
            String accountType=cmbAccountType.getValue();
            account.setAccountType(accountType);
            double balance=Double.parseDouble(txtInitialBalance.getText());
            account.setBalance(balance);
            boolean created = bankService.createAccount(account);
            if (created) {
            	Account createdAccount = bankService.getAccountByUserIdAccountType(SessionManager.getLoggedInUserId(),accountType);
            	SessionManager.setSelectedAccount(createdAccount);
            	System.out.println("Fetched account object: " + createdAccount);
            	System.out.println("Fetched account id: " + createdAccount.getAccountId());

            	String str = bankService.saveTransaction(createdAccount.getAccountId(),"DEPOSIT",balance);

            	System.out.println(str);

            } else {
                lblMessage.setText("Account Exists! Choose Different Account");
            }

        } catch (Exception e) {
            lblMessage.setText("This account type already exists");
            e.printStackTrace();
        }
    }
    public void goBack() {
    	service.GoBackService go=new service.GoBackService();
		go.goBackService(rootPane);
    }
}