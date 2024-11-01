import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    private FinanceManager financeManager;
    private JLabel balanceLabel;
    private JPanel transactionPanel;

    public MainFrame() {
        financeManager = new FinanceManager();

        setTitle("Personal Finance Manager");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initComponents();
    }

    private void initComponents() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        balanceLabel = new JLabel("Balance: ₹0.00", SwingConstants.LEFT);
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(balanceLabel, BorderLayout.WEST);

        JButton addButton = new JButton("+");
        addButton.setFont(new Font("Arial", Font.BOLD, 20));
        addButton.setPreferredSize(new Dimension(50, 50));
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAddTransactionDialog();
            }
        });
        headerPanel.add(addButton, BorderLayout.EAST);

        transactionPanel = new JPanel();
        transactionPanel.setLayout(new BoxLayout(transactionPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(transactionPanel);

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        updateBalance();
        updateTransactionArea();
    }

    private void openAddTransactionDialog() {
        JDialog dialog = new JDialog(this, "Add Transaction", true);
        dialog.setSize(300, 300);
        dialog.setLayout(new GridLayout(5, 2));

        dialog.add(new JLabel("Description:"));
        JTextField descriptionField = new JTextField();
        dialog.add(descriptionField);

        dialog.add(new JLabel("Amount:"));
        JTextField amountField = new JTextField();
        dialog.add(amountField);

        dialog.add(new JLabel("Type:"));
        String[] types = {"Income", "Expense"};
        JComboBox<String> typeCombo = new JComboBox<>(types);
        dialog.add(typeCombo);

        dialog.add(new JLabel("Category:"));
        JTextField categoryField = new JTextField();
        dialog.add(categoryField);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String description = descriptionField.getText();
                double amount = Double.parseDouble(amountField.getText());
                String type = (String) typeCombo.getSelectedItem();
                String category = categoryField.getText();

                financeManager.addTransaction(description, amount, type, category);

                updateBalance();
                updateTransactionArea();

                dialog.dispose();
            }
        });
        dialog.add(saveButton);

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void updateBalance() {
        double balance = financeManager.getBalance();
        balanceLabel.setText("Balance: ₹" + String.format("%.2f", balance));
    }

    private void updateTransactionArea() {
        transactionPanel.removeAll();

        Transaction[] transactions = financeManager.getTransactions();
        for (Transaction transaction : transactions) {
            JPanel transactionEntry = new JPanel(new GridLayout(2, 1));
            transactionEntry.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
            transactionEntry.setBackground(transaction.getType().equals("Income") ? new Color(204, 255, 204) : new Color(255, 204, 204));
            transactionEntry.setPreferredSize(new Dimension(350, 60));

            String amountText = "₹" + String.format("%.2f", Math.abs(transaction.getAmount()));
            JLabel descriptionLabel = new JLabel("Transaction #" + transaction.getTransactionNumber() + ": " + transaction.getDescription() + " - " + amountText);
            JLabel detailsLabel = new JLabel(transaction.getCategory() + " (" + transaction.getType() + ")");
            descriptionLabel.setFont(new Font("Arial", Font.BOLD, 12));
            detailsLabel.setFont(new Font("Arial", Font.PLAIN, 10));

            transactionEntry.add(descriptionLabel);
            transactionEntry.add(detailsLabel);

            transactionPanel.add(transactionEntry);
        }

        transactionPanel.revalidate();
        transactionPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
