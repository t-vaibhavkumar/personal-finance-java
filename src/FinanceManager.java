public class FinanceManager {
    private Transaction[] transactions;
    private int transactionCount; // Tracks the number of transactions added
    private int nextTransactionNumber; // Unique transaction number generator

    public FinanceManager() {
        transactions = new Transaction[1000]; // Fixed-size array for up to 1000 transactions
        transactionCount = 0;
        nextTransactionNumber = 1; // Start transaction numbers from 1
    }

    // Adds a transaction, assigning a unique transaction number internally
    public void addTransaction(String description, double amount, String type, String category) {
        if (transactionCount < transactions.length) {
            Transaction transaction = new Transaction(nextTransactionNumber, description, amount, type, category);
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
            balance += transactions[i].getAmount();
        }
        return balance;
    }

    public Transaction[] getTransactions() {
        // Return a trimmed array of current transactions only
        Transaction[] currentTransactions = new Transaction[transactionCount];
        System.arraycopy(transactions, 0, currentTransactions, 0, transactionCount);
        return currentTransactions;
    }
}
