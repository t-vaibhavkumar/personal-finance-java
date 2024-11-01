import java.util.ArrayList;

public class FinanceManager {
    private Transaction[] transactions;
    private int transactionCount;
    private int nextTransactionNumber;

    public FinanceManager() {
        transactions = new Transaction[1000]; // Fixed-size array
        transactionCount = 0;
        nextTransactionNumber = 1; // Start numbering transactions from 1
    }

    public void addTransaction(String description, double amount, String type, String category) {
        if (transactionCount < transactions.length) {
            Transaction transaction;
            if ("Income".equalsIgnoreCase(type)) {
                transaction = new IncomeTransaction(nextTransactionNumber, description, amount, category);
            } else {
                transaction = new ExpenseTransaction(nextTransactionNumber, description, amount, category);
            }
            transactions[transactionCount] = transaction;
            transactionCount++;
            nextTransactionNumber++;
        } else {
            System.out.println("Transaction limit reached.");
        }
    }

    public double getBalance() {
        double balance = 0;
        for (int i = 0; i < transactionCount; i++) {
            balance += transactions[i].getAmount(); // Polymorphic call to getAmount()
        }
        return balance;
    }

    public Transaction[] getTransactions() {
        Transaction[] currentTransactions = new Transaction[transactionCount];
        System.arraycopy(transactions, 0, currentTransactions, 0, transactionCount);
        return currentTransactions;
    }
}
