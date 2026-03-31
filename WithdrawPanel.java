package banking.ui;

import banking.service.BankingService;
import banking.service.ServiceResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Withdraw amount panel.
 */
public class WithdrawPanel extends JPanel {

    private final BankingService service;
    private final MainFrame      mainFrame;

    private final JTextField     tfAmount;
    private final JTextField     tfDescription;

    public WithdrawPanel(BankingService service, MainFrame mainFrame) {
        this.service   = service;
        this.mainFrame = mainFrame;

        setLayout(new GridBagLayout());
        setBackground(Theme.BG);

        JPanel card = Theme.card();
        card.setLayout(new BorderLayout(0, 20));
        card.setPreferredSize(new Dimension(420, 340));

        // ── Header ────────────────────────────────────────────────────────
        JPanel header = new JPanel(new GridLayout(2, 1, 0, 4));
        header.setBackground(Theme.CARD_BG);

        JLabel icon = new JLabel("💸", SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));

        JLabel title = new JLabel("Withdraw Amount", SwingConstants.CENTER);
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.DANGER);

        header.add(icon);
        header.add(title);
        card.add(header, BorderLayout.NORTH);

        // ── Form ──────────────────────────────────────────────────────────
        JPanel form = new JPanel(new GridLayout(4, 1, 0, 10));
        form.setBackground(Theme.CARD_BG);

        form.add(Theme.fieldLabel("Amount (₹)"));
        tfAmount = Theme.textField(20);
        form.add(tfAmount);

        form.add(Theme.fieldLabel("Description (optional)"));
        tfDescription = Theme.textField(20);
        tfDescription.setText("Cash Withdrawal");
        form.add(tfDescription);

        card.add(form, BorderLayout.CENTER);

        // ── Buttons ───────────────────────────────────────────────────────
        JPanel btnPanel = new JPanel(new GridLayout(2, 1, 0, 8));
        btnPanel.setBackground(Theme.CARD_BG);

        JButton btnWithdraw = Theme.dangerButton("Withdraw");
        btnWithdraw.addActionListener(this::onWithdraw);

        JButton btnBack = Theme.secondaryButton("← Dashboard");
        btnBack.addActionListener(e -> {
            reset();
            mainFrame.showPanel("DASHBOARD");
        });

        btnPanel.add(btnWithdraw);
        btnPanel.add(btnBack);
        card.add(btnPanel, BorderLayout.SOUTH);

        add(card);
    }

    private void onWithdraw(ActionEvent e) {
        double amount;
        try {
            amount = Double.parseDouble(tfAmount.getText().trim());
        } catch (NumberFormatException ex) {
            Theme.showError(this, "Please enter a valid numeric amount.");
            return;
        }

        String desc  = tfDescription.getText().trim();
        String accNo = mainFrame.getLoggedInAccountNo();

        double balance = service.getBalance(accNo);
        if (!Theme.showConfirm(this,
                String.format("Confirm withdrawal of ₹%.2f?\nCurrent balance: ₹%.2f", amount, balance)))
            return;

        ServiceResult result = service.withdraw(accNo, amount, desc);
        if (result.isSuccess()) {
            reset();
            Theme.showSuccess(this, result.getMessage());
            mainFrame.getDashboard().refreshBalance();
            mainFrame.showPanel("DASHBOARD");
        } else {
            Theme.showError(this, result.getMessage());
        }
    }

    public void reset() {
        tfAmount.setText("");
        tfDescription.setText("Cash Withdrawal");
    }
}
