public class IncomeTransaction extends Transaction {
    private double amount;

    public IncomeTransaction(int transactionNumber, String description, double amount, String category) {
        super(transactionNumber, description, category);
        this.amount = amount;
    }

    @Override
    public double getAmount() {
        return amount; // Positive amount for income
    }

    @Override
    public String getType() {
        return "Income";
    }
}
