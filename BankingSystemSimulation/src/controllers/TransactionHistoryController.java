package controllers;

import dao.TransactionDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import models.Transaction;
import util.SessionManager;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionHistoryController {

    @FXML
    private BorderPane rootPane;

    @FXML
    private DatePicker dpFromDate;

    @FXML
    private DatePicker dpToDate;

    @FXML
    private ComboBox<String> cmbTransactionType;

    @FXML
    private TableView<Transaction> tblTransactions;

    @FXML
    private TableColumn<Transaction, String> colTransactionId;

    @FXML
    private TableColumn<Transaction, String> colType;

    @FXML
    private TableColumn<Transaction, Double> colAmount;

    @FXML
    private TableColumn<Transaction, Integer> colTargetAccount;

    @FXML
    private TableColumn<Transaction, Object> colDate;

    private final TransactionDAO transactionDAO =
            new TransactionDAO();

    private ObservableList<Transaction> allTransactions =
            FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        colTransactionId.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
        colType.setCellValueFactory(new PropertyValueFactory<>("transactionType"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colTargetAccount.setCellValueFactory(new PropertyValueFactory<>("targetAccountId"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
        
        cmbTransactionType.getItems().addAll("ALL","DEPOSIT","WITHDRAW","TRANSFER");
        cmbTransactionType.getSelectionModel().selectFirst();
        loadTransactions();
    }

    private void loadTransactions() {

        List<Transaction> list = transactionDAO.getTransactionHistoryByAccountId(SessionManager.getSelectedAccountId());
        allTransactions.setAll(list);
        tblTransactions.setItems(allTransactions);
    }

    @FXML
    public void applyFilters() {

        LocalDate fromDate = dpFromDate.getValue();
        LocalDate toDate = dpToDate.getValue();
        String type = cmbTransactionType.getValue();

        List<Transaction> filtered =allTransactions.stream().filter(transaction -> {
                            boolean matchesDate = true;
                            boolean matchesType = true;

                            if (fromDate != null) {
                                matchesDate =!transaction.getTransactionDate().toLocalDate().isBefore(fromDate);
                            }

                            if (toDate != null) {
                                matchesDate =matchesDate &&!transaction.getTransactionDate().toLocalDate().isAfter(toDate);
                            }

                            if (!"ALL".equals(type)) {
                                matchesType =transaction.getTransactionType().equalsIgnoreCase(type);
                            }

                            return matchesDate && matchesType;
                        }).collect(Collectors.toList());
        tblTransactions.setItems(FXCollections.observableArrayList(filtered));
    }

    @FXML
    public void resetFilters() {
        dpFromDate.setValue(null);
        dpToDate.setValue(null);
        cmbTransactionType.getSelectionModel().selectFirst();
        tblTransactions.setItems(allTransactions);
    }

    @FXML
    public void goBack() {
        service.GoBackService go =new service.GoBackService();
        go.goBackService(rootPane);
    }
}