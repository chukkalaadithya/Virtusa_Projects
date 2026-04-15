package controllers;

import java.time.LocalDateTime;
import java.util.List;

import dao.TransactionDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import models.Transaction;
import service.GoToService;
import util.SessionManager;

public class TransactionHistoryController {

    @FXML
    private BorderPane rootPane;

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
    private TableColumn<Transaction, LocalDateTime> colDate;

    private final TransactionDAO transactionDAO = new TransactionDAO();
    private final GoToService go = new GoToService();

    @FXML
    public void initialize() {

        colTransactionId.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
        colType.setCellValueFactory(new PropertyValueFactory<>("transactionType"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colTargetAccount.setCellValueFactory(new PropertyValueFactory<>("targetAccountId"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));

        cmbTransactionType.getItems().clear();
        cmbTransactionType.getItems().addAll("ALL","DEPOSIT","WITHDRAW","TRANSFER");
        cmbTransactionType.getSelectionModel().select("ALL");
        loadTransactions("ALL");
    }

    private void loadTransactions(String selectedType) {
        try {
            int accountId = SessionManager.getSelectedAccountId();
            List<Transaction> transactions =transactionDAO.getTransactionHistoryByAccountId(accountId);

            if (!"ALL".equalsIgnoreCase(selectedType)) {
                transactions.removeIf(transaction ->!transaction.getTransactionType().equalsIgnoreCase(selectedType));
            }

            tblTransactions.setItems(FXCollections.observableArrayList(transactions));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void applyFilters() {
        String selectedType = cmbTransactionType.getValue();

        if (selectedType == null || selectedType.isBlank()) {
            selectedType = "ALL";
        }

        loadTransactions(selectedType);
    }

    @FXML
    public void resetFilters() {
        cmbTransactionType.getSelectionModel().select("ALL");
        loadTransactions("ALL");
    }

    @FXML
    public void goBack() {
        go.goToPage("/view/dashboard.fxml","Dashboard",rootPane);
    }
}