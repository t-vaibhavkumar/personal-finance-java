public abstract class Transaction {
    protected int transactionNumber;
    protected String description;
    protected String category;
    protected static final double AMOUNT_LIMIT = 10_00_00_000; // 10 Crores

    public Transaction(int transactionNumber, String description, String category) {
        this.transactionNumber = transactionNumber;
        this.description = description;
        this.category = category;
    }

    public abstract double getAmount();
    public int getTransactionNumber() { return transactionNumber; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public abstract String getType(); // To identify if it's an Income or Expense transaction
}
