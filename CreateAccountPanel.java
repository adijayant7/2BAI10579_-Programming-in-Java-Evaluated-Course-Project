package banking.ui;

import banking.service.BankingService;
import banking.service.ServiceResult;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Account registration form.
 */
public class CreateAccountPanel extends JPanel {

    private final BankingService service;
    private final MainFrame      mainFrame;

    private final JTextField     tfName;
    private final JTextField     tfEmail;
    private final JTextField     tfPhone;
    private final JPasswordField pfPassword;
    private final JPasswordField pfConfirmPassword;

    public CreateAccountPanel(BankingService service, MainFrame mainFrame) {
        this.service   = service;
        this.mainFrame = mainFrame;

        setLayout(new GridBagLayout());
        setBackground(Theme.BG);

        JPanel card = Theme.card();
        card.setLayout(new BorderLayout(0, 16));
        card.setPreferredSize(new Dimension(450, 520));

        // ── Header ────────────────────────────────────────────────────────
        JLabel title = new JLabel("Create Account", SwingConstants.CENTER);
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.PRIMARY);
        JLabel sub = new JLabel("Open your account in seconds", SwingConstants.CENTER);
        sub.setFont(Theme.FONT_BODY);
        sub.setForeground(Theme.TEXT_GRAY);

        JPanel header = new JPanel(new GridLayout(2, 1, 0, 4));
        header.setBackground(Theme.CARD_BG);
        header.add(title);
        header.add(sub);
        card.add(header, BorderLayout.NORTH);

        // ── Form ──────────────────────────────────────────────────────────
        JPanel form = new JPanel(new GridLayout(10, 1, 0, 6));
        form.setBackground(Theme.CARD_BG);

        form.add(Theme.fieldLabel("Full Name"));
        tfName = Theme.textField(20);
        form.add(tfName);

        form.add(Theme.fieldLabel("Email Address"));
        tfEmail = Theme.textField(20);
        form.add(tfEmail);

        form.add(Theme.fieldLabel("Phone Number (10 digits)"));
        tfPhone = Theme.textField(20);
        form.add(tfPhone);

        form.add(Theme.fieldLabel("Password (min 6 chars)"));
        pfPassword = Theme.passwordField(20);
        form.add(pfPassword);

        form.add(Theme.fieldLabel("Confirm Password"));
        pfConfirmPassword = Theme.passwordField(20);
        form.add(pfConfirmPassword);

        card.add(form, BorderLayout.CENTER);

        // ── Buttons ───────────────────────────────────────────────────────
        JPanel btnPanel = new JPanel(new GridLayout(2, 1, 0, 8));
        btnPanel.setBackground(Theme.CARD_BG);

        JButton btnCreate = Theme.primaryButton("Create Account");
        btnCreate.addActionListener(this::onCreate);

        JButton btnBack = Theme.secondaryButton("← Back to Login");
        btnBack.addActionListener(e -> {
            reset();
            mainFrame.showPanel("LOGIN");
        });

        btnPanel.add(btnCreate);
        btnPanel.add(btnBack);
        card.add(btnPanel, BorderLayout.SOUTH);

        add(card);
    }

    private void onCreate(ActionEvent e) {
        String name     = tfName.getText().trim();
        String email    = tfEmail.getText().trim();
        String phone    = tfPhone.getText().trim();
        String pass     = new String(pfPassword.getPassword());
        String confirm  = new String(pfConfirmPassword.getPassword());

        if (!pass.equals(confirm)) {
            Theme.showError(this, "Passwords do not match.");
            return;
        }

        ServiceResult result = service.createAccount(name, email, phone, pass);
        if (result.isSuccess()) {
            reset();
            Theme.showSuccess(this,
                    result.getMessage() + "\n\nPlease login with your new account number.");
            mainFrame.showPanel("LOGIN");
        } else {
            Theme.showError(this, result.getMessage());
        }
    }

    public void reset() {
        tfName.setText("");
        tfEmail.setText("");
        tfPhone.setText("");
        pfPassword.setText("");
        pfConfirmPassword.setText("");
    }
}
