import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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
        dialog.setSize(300, 250);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        dialog.add(new JLabel("Description:"), gbc);

        gbc.gridx = 1;
        JTextField descriptionField = new JTextField(10);
        dialog.add(descriptionField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        dialog.add(new JLabel("Amount:"), gbc);

        gbc.gridx = 1;
        JTextField amountField = new JTextField(10);
        dialog.add(amountField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        dialog.add(new JLabel("Type:"), gbc);

        gbc.gridx = 1;
        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JCheckBox incomeCheckBox = new JCheckBox("Income");
        JCheckBox expenseCheckBox = new JCheckBox("Expense");

        // Ensuring only one checkbox is selected at a time
        incomeCheckBox.addActionListener(e -> {
            if (incomeCheckBox.isSelected()) {
                expenseCheckBox.setSelected(false);
            }
        });
        expenseCheckBox.addActionListener(e -> {
            if (expenseCheckBox.isSelected()) {
                incomeCheckBox.setSelected(false);
            }
        });

        typePanel.add(incomeCheckBox);
        typePanel.add(expenseCheckBox);
        dialog.add(typePanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        dialog.add(new JLabel("Category:"), gbc);

        gbc.gridx = 1;
        JTextField categoryField = new JTextField(10);
        dialog.add(categoryField, gbc);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String description = descriptionField.getText();
                    double amount = Double.parseDouble(amountField.getText());
                    String category = categoryField.getText();

                    // Check if amount is above the limit
                    if (amount > Transaction.AMOUNT_LIMIT) {
                        throw new AmountLimitExceededException("Amount exceeds the limit of 10 crores.");
                    }

                    // Check if only one checkbox is selected
                    if (!incomeCheckBox.isSelected() && !expenseCheckBox.isSelected()) {
                        throw new IllegalArgumentException("Please select either Income or Expense.");
                    }

                    String type = incomeCheckBox.isSelected() ? "Income" : "Expense";

                    // Add transaction to FinanceManager
                    financeManager.addTransaction(description, amount, type, category);

                    updateBalance();
                    updateTransactionArea();

                    dialog.dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Please enter a valid amount.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                } catch (AmountLimitExceededException ex) {
                    JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Amount Limit Exceeded", JOptionPane.ERROR_MESSAGE);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Selection Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 4;
        dialog.add(saveButton, gbc);

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
            JPanel transactionEntry = new JPanel();
            transactionEntry.setLayout(new BorderLayout());
            transactionEntry.setBorder(new CompoundBorder(
                    new LineBorder(new Color(200, 200, 200), 1, true),
                    new EmptyBorder(5, 5, 5, 5)
            ));
            transactionEntry.setBackground(transaction.getType().equals("Income") ? new Color(224, 255, 224) : new Color(255, 228, 225));
            transactionEntry.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50)); // Dynamic width, fixed height

            // Category label at the top
            JLabel categoryLabel = new JLabel(transaction.getCategory(), SwingConstants.LEFT);
            categoryLabel.setFont(new Font("Arial", Font.BOLD, 12));

            // Description and amount below the category
            String amountText = "₹" + String.format("%.2f", Math.abs(transaction.getAmount()));
            JLabel descriptionLabel = new JLabel(transaction.getDescription());
            descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 11));
            JLabel amountLabel = new JLabel(amountText, SwingConstants.RIGHT);
            amountLabel.setFont(new Font("Arial", Font.BOLD, 12));

            JPanel detailsPanel = new JPanel(new BorderLayout());
            detailsPanel.setOpaque(false);
            detailsPanel.add(descriptionLabel, BorderLayout.WEST);
            detailsPanel.add(amountLabel, BorderLayout.EAST);

            // Add delete button for each transaction
            JButton deleteButton = new JButton("Delete");
            deleteButton.addActionListener(evt -> {
                financeManager.deleteTransaction(transaction.getTransactionNumber());
                updateBalance();
                updateTransactionArea();
            });

            transactionEntry.add(categoryLabel, BorderLayout.NORTH);
            transactionEntry.add(detailsPanel, BorderLayout.CENTER);
            transactionEntry.add(deleteButton, BorderLayout.EAST); // Delete button aligned to the right

            transactionPanel.add(transactionEntry);
            transactionPanel.add(Box.createVerticalStrut(8)); // Adds spacing between entries
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
