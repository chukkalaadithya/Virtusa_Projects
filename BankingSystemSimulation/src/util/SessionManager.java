package util;

import models.Account;
import models.User;

public final class SessionManager {

    private static User loggedInUser;
    private static Account selectedAccount;

    public static void login(User user) {
        loggedInUser = user;
        selectedAccount = null;
    }

    public static void logout() {
        loggedInUser = null;
        selectedAccount = null;
    }

    public static boolean isLoggedIn() {
        return loggedInUser != null;
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static int getLoggedInUserId() {
        return loggedInUser != null? loggedInUser.getUserId(): 0;
    }

    public static String getUsername() {
        return loggedInUser != null? loggedInUser.getUserName(): "";
    }

    public static String getFullName() {
        return loggedInUser != null? loggedInUser.getFullName(): "";
    }

    public static void setSelectedAccount(Account account) {
        selectedAccount = account;
    }

    public static Account getSelectedAccount() {
        return selectedAccount;
    }

    public static int getSelectedAccountId() {
        return selectedAccount != null? selectedAccount.getAccountId(): 0;
    }

    public static boolean hasSelectedAccount() {
        return selectedAccount != null;
    }

    public static void clearSelectedAccount() {
        selectedAccount = null;
    }
}