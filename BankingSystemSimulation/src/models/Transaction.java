package models;

import java.time.LocalDateTime;

public class Transaction {

    private String transactionId;
    private int accountId;
    private Integer targetAccountId;
    private String transactionType;
    private double amount;
    private LocalDateTime transactionDate;

    public Transaction() {
    }

    public Transaction(String transactionId,int accountId,Integer targetAccountId,String transactionType,double amount) {

        this.transactionId = transactionId;
        this.accountId = accountId;
        this.targetAccountId = targetAccountId;
        this.transactionType = transactionType;
        this.amount = amount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public Integer getTargetAccountId() {
        return targetAccountId;
    }

    public void setTargetAccountId(Integer targetAccountId) {
        this.targetAccountId = targetAccountId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }
}