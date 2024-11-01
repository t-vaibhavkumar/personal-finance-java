public class ExpenseTransaction extends Transaction {
    private double amount;

    public ExpenseTransaction(int transactionNumber, String description, double amount, String category) {
        super(transactionNumber, description, category);
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
