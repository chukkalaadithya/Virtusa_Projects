package controllers;

import dao.AccountDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Account;
import models.User;
import service.BankService;
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

    private BankService bankService = new BankService();
    SessionManager session=new SessionManager();
    private AccountDAO accountDao=new AccountDAO();
    private int accountId = 1;

    @FXML
    public void transferMoney() {
        try {
            int targetAccountId = Integer.parseInt(txtTargetAccount.getText());
            double amount = Double.parseDouble(txtAmount.getText());
            double balance=session.getSelectedAccount().getBalance();
            Account checkId=bankService.getAccountById(targetAccountId);
            if(targetAccountId==session.getSelectedAccount().getAccountId()) {
            	lblMessage.setText("from and to account Id cannot be Same");
            	return;
            }
            if(checkId==null) {
            	lblMessage.setText("Enter Correct Target Account Id");
            	return;
            }
            if(amount<=0) {
            	lblMessage.setText("Enter Amount greater than Zero");
            	return;
            }
            if(amount>balance) {
            	lblMessage.setText("Insuffient Balance to Transfer Amount");
            	return;
            }
            try {
	            if(amount>0 && balance>=amount) {
	            	double updatedBalance=balance-amount;
	            	boolean withdrawAmount=bankService.updateBalance(session.getSelectedAccountId(),updatedBalance);
	            	double targetUpdatedBalance=checkId.getBalance()+amount;
	            	boolean depositAmount=bankService.updateBalance(targetAccountId,targetUpdatedBalance);
	            	if(withdrawAmount && depositAmount) {
	            		session.getSelectedAccount().setBalance(updatedBalance);
	            		lblMessage.setText("Amount "+amount+" Succesfully Transfered to ***"+targetAccountId);
	            		String str=bankService.saveTransaction(targetAccountId,"TRANSFER", amount);
	            		System.out.println(str);
	            	}
	            	txtTargetAccount.clear();
	            	txtAmount.clear();
	            	return;
	            }
            }
            catch(Exception e) {
            	lblMessage.setText("Error in Account Tranfer");
            	e.printStackTrace();
            }

        } catch (Exception e) {
            lblMessage.setText("Enter valid details");
        }
    }

    @FXML
    public void goBack() {
    	service.GoBackService go=new service.GoBackService();
    	go.goBackService(rootPane);
    }
}