import javax.swing.*;
import java.util.ArrayList;

public class FinanceManager {
    private ArrayList<Transaction> transactions; // Changed to ArrayList for easier dynamic management
    private int nextTransactionNumber;

    public FinanceManager() {
        transactions = new ArrayList<>(); // Use ArrayList instead of a fixed-size array
        nextTransactionNumber = 1; // Start numbering transactions from 1
    }

    public void addTransaction(String description, double amount, String type, String category) {
        try {
            // Validation checks with alert dialogs
            if (description == null || description.isEmpty()) {
                throw new IllegalArgumentException("Description cannot be empty.");
            }
            if (category == null || category.isEmpty()) {
                throw new IllegalArgumentException("Category cannot be empty.");
            }
            if (amount < 0) {
                throw new IllegalArgumentException("Amount must be non-negative.");
            }

            Transaction transaction;
            if ("Income".equalsIgnoreCase(type)) {
                transaction = new IncomeTransaction(nextTransactionNumber, description, amount, category);
            } else if ("Expense".equalsIgnoreCase(type)) {
                transaction = new ExpenseTransaction(nextTransactionNumber, description, amount, category);
            } else {
                throw new IllegalArgumentException("Invalid transaction type. Must be 'Income' or 'Expense'.");
            }

            transactions.add(transaction);
            nextTransactionNumber++;

        } catch (AmountLimitExceededException e) {
            showAlert("Transaction failed: " + e.getMessage(), "Amount Limit Exceeded");
        } catch (IllegalArgumentException e) {
            showAlert("Transaction failed: " + e.getMessage(), "Input Error");
        }
    }

    public double getBalance() {
        double balance = 0;
        for (Transaction transaction : transactions) {
            balance += transaction.getAmount(); // Polymorphic call to getAmount()
        }
        return balance;
    }

    public Transaction[] getTransactions() {
        return transactions.toArray(new Transaction[0]); // Convert ArrayList to array for compatibility
    }

    // New method to delete a transaction by transactionNumber
    public void deleteTransaction(int transactionNumber) {
        transactions.removeIf(transaction -> transaction.getTransactionNumber() == transactionNumber);
    }

    // Helper method to show alert dialogs
    private void showAlert(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }
}
