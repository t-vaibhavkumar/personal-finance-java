public class ExpenseTransaction extends Transaction {
    private double amount;

    public ExpenseTransaction(int transactionNumber, String description, double amount, String category) throws AmountLimitExceededException {
        super(transactionNumber, description, category);
        if (amount < 0) throw new IllegalArgumentException("Amount must be non-negative.");
        if (amount > AMOUNT_LIMIT) throw new AmountLimitExceededException("Amount exceeds the limit of 10 crores.");
        this.amount = amount;
    }

    @Override
    public double getAmount() {
        return -amount; // Negative amount for expense
    }

    @Override
    public String getType() {
        return "Expense";
    }
}
