package banking.ui;

import banking.service.BankingService;
import banking.service.ServiceResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Change phone number panel.
 */
public class ChangePhonePanel extends JPanel {

    private final BankingService service;
    private final MainFrame      mainFrame;

    private final JTextField     tfNewPhone;
    private final JPasswordField pfPassword;

    public ChangePhonePanel(BankingService service, MainFrame mainFrame) {
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

        JLabel icon = new JLabel("📱", SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));

        JLabel title = new JLabel("Change Phone Number", SwingConstants.CENTER);
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(new Color(107, 70, 193));

        header.add(icon);
        header.add(title);
        card.add(header, BorderLayout.NORTH);

        // ── Form ──────────────────────────────────────────────────────────
        JPanel form = new JPanel(new GridLayout(4, 1, 0, 10));
        form.setBackground(Theme.CARD_BG);

        form.add(Theme.fieldLabel("New Phone Number (10 digits)"));
        tfNewPhone = Theme.textField(20);
        form.add(tfNewPhone);

        form.add(Theme.fieldLabel("Confirm with Password"));
        pfPassword = Theme.passwordField(20);
        form.add(pfPassword);

        card.add(form, BorderLayout.CENTER);

        // ── Buttons ───────────────────────────────────────────────────────
        JPanel btnPanel = new JPanel(new GridLayout(2, 1, 0, 8));
        btnPanel.setBackground(Theme.CARD_BG);

        JButton btnUpdate = Theme.primaryButton("Update Phone Number");
        btnUpdate.addActionListener(this::onUpdate);

        JButton btnBack = Theme.secondaryButton("← Dashboard");
        btnBack.addActionListener(e -> {
            reset();
            mainFrame.showPanel("DASHBOARD");
        });

        btnPanel.add(btnUpdate);
        btnPanel.add(btnBack);
        card.add(btnPanel, BorderLayout.SOUTH);

        add(card);
    }

    private void onUpdate(ActionEvent e) {
        String newPhone  = tfNewPhone.getText().trim();
        String password  = new String(pfPassword.getPassword());

        ServiceResult result = service.changePhoneNumber(
                mainFrame.getLoggedInAccountNo(), password, newPhone);

        if (result.isSuccess()) {
            reset();
            Theme.showSuccess(this, result.getMessage());
            mainFrame.showPanel("DASHBOARD");
        } else {
            Theme.showError(this, result.getMessage());
        }
    }

    public void reset() {
        tfNewPhone.setText("");
        pfPassword.setText("");
    }
}
