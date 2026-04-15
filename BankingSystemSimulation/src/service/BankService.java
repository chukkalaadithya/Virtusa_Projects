package service;

import java.util.List;

import dao.AccountDAO;
import dao.TransactionDAO;
import dao.UserDAO;
import exceptions.BankingSystemException;
import exceptions.InvalidCredentialException;
import exceptions.ServiceLayerException;
import models.Account;
import models.Transaction;
import models.User;
import util.SessionManager;

public class BankService {
	
	private final UserDAO userDAO=new UserDAO();
	private final TransactionDAO transactionDAO=new TransactionDAO();
    private final AccountDAO accountDAO = new AccountDAO();
    
    public User loginUser(String userName, String password) throws InvalidCredentialException,Exception{  //include
	    User user = userDAO.login(userName, password);
	    if (user == null) {
	        throw new InvalidCredentialException("Invalid username or password");
	    }
	    return user;
    }
    
    public boolean registerUser(User user) throws Exception{   //include
    	return userDAO.registerUser(user);
    }
    
    public User getUserByUsername(String username) throws Exception{ //include
	    User user=userDAO.getUserByUsername(username);
	    return user;
    }

    public List<Account> getAccountsByUserId(int userId) {  //include
        return accountDAO.getAccountsByUserId(userId);
    }
    
    public boolean createAccount(Account account) {
        return  accountDAO.createAccount(account);
    }
    public double checkBalance(int UserId,int AccountId) {
    	return accountDAO.getBalance(UserId,AccountId);
    }

    public boolean updateBalance(int accountId, double balance) {
        return accountDAO.updateBalance(accountId, balance);
    }
    
    public Account getAccountByUserIdAccountType(int userId,String accountType) {
    	return accountDAO.getAccountByUserIdAccountType(userId, accountType);
    }

    public Account getAccountById(int accountId) {
        return accountDAO.getAccountById(accountId);
    }
    
    public String saveTransaction(int targetAccountId,String transactionType, double amount) {
    	if(String.valueOf(targetAccountId)!=null && amount>0) {
	    	String transactionId=generateTransactionId(targetAccountId,transactionType);
	    	Transaction t= new Transaction(transactionId,SessionManager.getSelectedAccountId(),targetAccountId,transactionType,amount);
	    	if(transactionDAO.saveTransaction(t)) {
	    		return "Transaction Successfully Saved";
	    	}
	    	else {
	    		return "tranasction Save Failed";
	    	}
    	}
    	else {
    		return "Enter Correct TragetAccountId or amount";
    	}
    	
    }

    private String generateTransactionId(int targetAccountId,String transactionType) {
    	return "TXN-" + transactionType.substring(0, 3).toUpperCase()+System.currentTimeMillis();
    }
}