package banking.ui;

import banking.service.BankingService;
import banking.service.ServiceResult;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Login screen – collects Account Number + Password.
 */
public class LoginPanel extends JPanel {

    private final BankingService service;
    private final MainFrame      mainFrame;

    private final JTextField     tfAccountNo;
    private final JPasswordField pfPassword;

    public LoginPanel(BankingService service, MainFrame mainFrame) {
        this.service   = service;
        this.mainFrame = mainFrame;

        setLayout(new GridBagLayout());
        setBackground(Theme.BG);

        JPanel card = Theme.card();
        card.setLayout(new BorderLayout(0, 20));
        card.setPreferredSize(new Dimension(420, 400));

        // ── Header ────────────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.CARD_BG);

        JLabel icon = new JLabel("🏦", SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));

        JLabel title = new JLabel("Welcome Back", SwingConstants.CENTER);
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.PRIMARY);

        JLabel sub = new JLabel("Sign in to your account", SwingConstants.CENTER);
        sub.setFont(Theme.FONT_BODY);
        sub.setForeground(Theme.TEXT_GRAY);

        JPanel titleBox = new JPanel(new GridLayout(3, 1, 0, 4));
        titleBox.setBackground(Theme.CARD_BG);
        titleBox.add(icon);
        titleBox.add(title);
        titleBox.add(sub);
        header.add(titleBox, BorderLayout.CENTER);
        card.add(header, BorderLayout.NORTH);

        // ── Form ──────────────────────────────────────────────────────────
        JPanel form = new JPanel(new GridLayout(4, 1, 0, 10));
        form.setBackground(Theme.CARD_BG);

        form.add(Theme.fieldLabel("Account Number"));
        tfAccountNo = Theme.textField(20);
        tfAccountNo.setToolTipText("e.g. ACC123456789");
        form.add(tfAccountNo);

        form.add(Theme.fieldLabel("Password"));
        pfPassword = Theme.passwordField(20);
        form.add(pfPassword);

        card.add(form, BorderLayout.CENTER);

        // ── Buttons ───────────────────────────────────────────────────────
        JPanel btnPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        btnPanel.setBackground(Theme.CARD_BG);

        JButton btnLogin = Theme.primaryButton("Login");
        btnLogin.setPreferredSize(new Dimension(0, 44));
        btnLogin.addActionListener(this::onLogin);

        JButton btnCreate = Theme.secondaryButton("Create New Account");
        btnCreate.setPreferredSize(new Dimension(0, 40));
        btnCreate.addActionListener(e -> mainFrame.showPanel("CREATE_ACCOUNT"));

        btnPanel.add(btnLogin);
        btnPanel.add(btnCreate);
        card.add(btnPanel, BorderLayout.SOUTH);

        add(card);

        // Allow Enter key to trigger login
        getRootPane().setDefaultButton(btnLogin);
    }

    private void onLogin(ActionEvent e) {
        String accNo    = tfAccountNo.getText().trim();
        String password = new String(pfPassword.getPassword());

        ServiceResult result = service.login(accNo, password);
        if (result.isSuccess()) {
            pfPassword.setText("");
            Theme.showSuccess(this, result.getMessage());
            mainFrame.loginSuccess(result.getData()); // passes account number
        } else {
            Theme.showError(this, result.getMessage());
        }
    }

    /** Clears inputs when the panel is shown again (e.g. after logout). */
    public void reset() {
        tfAccountNo.setText("");
        pfPassword.setText("");
    }
}
