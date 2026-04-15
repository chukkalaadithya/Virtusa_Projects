package service;

import java.util.List;

import dao.AccountDAO;
import dao.TransactionDAO;
import dao.UserDAO;
import exceptions.InvalidCredentialException;
import exceptions.ServiceLayerException;
import models.Account;
import models.Transaction;
import models.User;
import util.SessionManager;

public class BankService {

    private final UserDAO userDAO = new UserDAO();
    private final AccountDAO accountDAO = new AccountDAO();
    private final TransactionDAO transactionDAO = new TransactionDAO();

    public User loginUser(String userName, String password) throws InvalidCredentialException, ServiceLayerException {
        try {
            User user = userDAO.login(userName, password);
            if (user == null) {
                throw new InvalidCredentialException("Invalid username or password");
            }
            SessionManager.login(user);
            return user;

        } catch (InvalidCredentialException e) {
            throw e;

        } catch (Exception e) {
            throw new ServiceLayerException("Unable to login. Please try again.",e);
        }
    }

    public boolean registerUser(User user) throws ServiceLayerException {
        try {
            return userDAO.registerUser(user);

        } catch (Exception e) {
            throw new ServiceLayerException("Registration failed",e);
        }
    }

    public User getUserByUsername(String username)
            throws ServiceLayerException {
        try {
            return userDAO.getUserByUsername(username);

        } catch (Exception e) {
            throw new ServiceLayerException("Unable to fetch user details",e);
        }
    }

    public List<Account> getAccountsByUserId(int userId) throws ServiceLayerException {
        try {
            return accountDAO.getAccountsByUserId(userId);

        } catch (Exception e) {
            throw new ServiceLayerException("Unable to fetch accounts",e);
        }
    }

    public boolean createAccount(Account account) throws ServiceLayerException {
        try {
            return accountDAO.createAccount(account);

        } catch (Exception e) {
            throw new ServiceLayerException("Unable to create account",e);
        }
    }

    public Account getAccountByUserIdAccountType(int userId, String accountType) throws ServiceLayerException {
        try {
            return accountDAO.getAccountByUserIdAccountType(userId,accountType);

        } catch (Exception e) {
            throw new ServiceLayerException("Unable to fetch account",e);
        }
    }

    public Account getAccountById(int accountId) throws ServiceLayerException {
        try {
            return accountDAO.getAccountById(accountId);

        } catch (Exception e) {
            throw new ServiceLayerException("Unable to fetch account",e);
        }
    }

    public boolean updateBalance(int accountId, double balance) throws ServiceLayerException {
        try {
            return accountDAO.updateBalance(accountId, balance);

        } catch (Exception e) {
            throw new ServiceLayerException("Unable to update balance",e);
        }
    }

    public double checkBalance(int userId, int accountId) throws ServiceLayerException {
        try {
            return accountDAO.getBalance(userId, accountId);

        } catch (Exception e) {
            throw new ServiceLayerException("Unable to check balance",e);
        }
    }

    public boolean saveTransaction(int targetAccountId,String transactionType,double amount) throws ServiceLayerException {

        try {
            if (amount <= 0) {
                throw new ServiceLayerException("Amount must be greater than zero");
            }

            String transactionId = generateTransactionId(transactionType);
            Transaction transaction = new Transaction(transactionId,SessionManager.getSelectedAccountId(),targetAccountId,transactionType,amount);
            return transactionDAO.saveTransaction(transaction);

        } catch (ServiceLayerException e) {
            throw e;

        } catch (Exception e) {
            throw new ServiceLayerException("Transaction save failed",e);
        }
    }
    public boolean updateUserProfile(User user) throws ServiceLayerException {
        try {
            return userDAO.updateUser(user);

        } catch (Exception e) {
            throw new ServiceLayerException("Unable to update profile",e);
        }
    }
    public boolean deleteUser(int userId) throws ServiceLayerException {
        try {
            transactionDAO.deleteTransactionsByUserId(userId);
            accountDAO.deleteAccountsByUserId(userId);
            return userDAO.deleteUser(userId);

        } catch (Exception e) {
            throw new ServiceLayerException("Unable to delete profile",e);
        }
    }

    private String generateTransactionId(String transactionType) {
        return "TXN-"+ transactionType.substring(0, 3).toUpperCase()+ System.currentTimeMillis();
    }
}