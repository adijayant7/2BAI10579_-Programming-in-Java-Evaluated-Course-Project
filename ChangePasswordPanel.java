package banking.ui;

import banking.service.BankingService;
import banking.service.ServiceResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Change password panel.
 */
public class ChangePasswordPanel extends JPanel {

    private final BankingService service;
    private final MainFrame      mainFrame;

    private final JPasswordField pfOld;
    private final JPasswordField pfNew;
    private final JPasswordField pfConfirm;

    public ChangePasswordPanel(BankingService service, MainFrame mainFrame) {
        this.service   = service;
        this.mainFrame = mainFrame;

        setLayout(new GridBagLayout());
        setBackground(Theme.BG);

        JPanel card = Theme.card();
        card.setLayout(new BorderLayout(0, 20));
        card.setPreferredSize(new Dimension(420, 380));

        // ── Header ────────────────────────────────────────────────────────
        JPanel header = new JPanel(new GridLayout(2, 1, 0, 4));
        header.setBackground(Theme.CARD_BG);

        JLabel icon = new JLabel("🔒", SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));

        JLabel title = new JLabel("Change Password", SwingConstants.CENTER);
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.PRIMARY_DARK);

        header.add(icon);
        header.add(title);
        card.add(header, BorderLayout.NORTH);

        // ── Form ──────────────────────────────────────────────────────────
        JPanel form = new JPanel(new GridLayout(6, 1, 0, 8));
        form.setBackground(Theme.CARD_BG);

        form.add(Theme.fieldLabel("Current Password"));
        pfOld = Theme.passwordField(20);
        form.add(pfOld);

        form.add(Theme.fieldLabel("New Password (min 6 chars)"));
        pfNew = Theme.passwordField(20);
        form.add(pfNew);

        form.add(Theme.fieldLabel("Confirm New Password"));
        pfConfirm = Theme.passwordField(20);
        form.add(pfConfirm);

        card.add(form, BorderLayout.CENTER);

        // ── Buttons ───────────────────────────────────────────────────────
        JPanel btnPanel = new JPanel(new GridLayout(2, 1, 0, 8));
        btnPanel.setBackground(Theme.CARD_BG);

        JButton btnChange = Theme.primaryButton("Change Password");
        btnChange.addActionListener(this::onChange);

        JButton btnBack = Theme.secondaryButton("← Dashboard");
        btnBack.addActionListener(e -> {
            reset();
            mainFrame.showPanel("DASHBOARD");
        });

        btnPanel.add(btnChange);
        btnPanel.add(btnBack);
        card.add(btnPanel, BorderLayout.SOUTH);

        add(card);
    }

    private void onChange(ActionEvent e) {
        String oldPwd  = new String(pfOld.getPassword());
        String newPwd  = new String(pfNew.getPassword());
        String confirm = new String(pfConfirm.getPassword());

        if (!newPwd.equals(confirm)) {
            Theme.showError(this, "New passwords do not match.");
            return;
        }

        ServiceResult result = service.changePassword(
                mainFrame.getLoggedInAccountNo(), oldPwd, newPwd);

        if (result.isSuccess()) {
            reset();
            Theme.showSuccess(this, result.getMessage());
            mainFrame.showPanel("DASHBOARD");
        } else {
            Theme.showError(this, result.getMessage());
        }
    }

    public void reset() {
        pfOld.setText("");
        pfNew.setText("");
        pfConfirm.setText("");
    }
}
