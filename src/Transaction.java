public class Transaction {
    private final int transactionNumber;
    private String description;
    private double amount;
    private String type; // "Income" or "Expense"
    private String category;

    public Transaction(int transactionNumber, String description, double amount, String type, String category) {
        this.transactionNumber = transactionNumber;
        this.description = description;
        this.amount = amount;
        this.type = type;
        this.category = category;
    }

    // The transaction number is kept private and is not accessible outside this class.
    public int getTransactionNumber() {
        return transactionNumber;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return type.equals("Income") ? amount : -amount;
    }

    public String getType() {
        return type;
    }

    public String getCategory() {
        return category;
    }
}
