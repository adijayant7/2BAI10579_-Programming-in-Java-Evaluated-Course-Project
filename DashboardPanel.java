package banking.ui;

import banking.model.Account;
import banking.service.BankingService;
import banking.service.ServiceResult;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Main dashboard shown after successful login.
 * Provides quick-action tiles and a sidebar for navigation.
 */
public class DashboardPanel extends JPanel {

    private final BankingService service;
    private final MainFrame      mainFrame;
    private       String         loggedInAccountNo;

    // header labels updated on login
    private final JLabel lblWelcome  = new JLabel();
    private final JLabel lblAccNo    = new JLabel();
    private final JLabel lblBalance  = new JLabel();

    public DashboardPanel(BankingService service, MainFrame mainFrame) {
        this.service   = service;
        this.mainFrame = mainFrame;

        setLayout(new BorderLayout());
        setBackground(Theme.BG);

        // ── Top nav bar ───────────────────────────────────────────────────
        JPanel topBar = buildTopBar();
        add(topBar, BorderLayout.NORTH);

        // ── Balance card ──────────────────────────────────────────────────
        JPanel center = new JPanel(new BorderLayout(0, 20));
        center.setBackground(Theme.BG);
        center.setBorder(new EmptyBorder(20, 30, 20, 30));

        center.add(buildBalanceCard(), BorderLayout.NORTH);
        center.add(buildActionGrid(),  BorderLayout.CENTER);

        add(center, BorderLayout.CENTER);
    }

    // ── Build helpers ─────────────────────────────────────────────────────

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(Theme.PRIMARY);
        bar.setBorder(new EmptyBorder(14, 24, 14, 24));

        JLabel bank = new JLabel("🏦  BankingSystem");
        bank.setFont(new Font("Segoe UI", Font.BOLD, 18));
        bank.setForeground(Color.WHITE);
        bar.add(bank, BorderLayout.WEST);

        lblWelcome.setFont(Theme.FONT_BODY);
        lblWelcome.setForeground(Color.WHITE);
        bar.add(lblWelcome, BorderLayout.CENTER);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setFont(Theme.FONT_BODY);
        btnLogout.setForeground(Theme.PRIMARY);
        btnLogout.setBackground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogout.addActionListener(this::onLogout);
        bar.add(btnLogout, BorderLayout.EAST);

        return bar;
    }

    private JPanel buildBalanceCard() {
        JPanel card = new JPanel(new GridLayout(3, 1, 0, 6));
        card.setBackground(Theme.PRIMARY);
        card.setBorder(new EmptyBorder(24, 28, 24, 28));

        JLabel lbl1 = new JLabel("Available Balance");
        lbl1.setFont(Theme.FONT_BODY);
        lbl1.setForeground(Theme.PRIMARY_LIGHT);

        lblBalance.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblBalance.setForeground(Color.WHITE);

        lblAccNo.setFont(Theme.FONT_SMALL);
        lblAccNo.setForeground(Theme.PRIMARY_LIGHT);

        card.add(lbl1);
        card.add(lblBalance);
        card.add(lblAccNo);
        return card;
    }

    private JPanel buildActionGrid() {
        JPanel grid = new JPanel(new GridLayout(2, 4, 14, 14));
        grid.setBackground(Theme.BG);

        grid.add(tile("💰", "Deposit",      Theme.SUCCESS,  e -> mainFrame.showPanel("DEPOSIT")));
        grid.add(tile("💸", "Withdraw",     Theme.DANGER,   e -> mainFrame.showPanel("WITHDRAW")));
        grid.add(tile("📊", "Balance",      Theme.PRIMARY,  e -> showBalance()));
        grid.add(tile("📋", "Transactions", Theme.WARNING,  e -> mainFrame.showPanel("TRANSACTIONS")));
        grid.add(tile("🔒", "Change Pwd",   Theme.PRIMARY_DARK, e -> mainFrame.showPanel("CHANGE_PASSWORD")));
        grid.add(tile("📱", "Change Phone", new Color(107, 70, 193), e -> mainFrame.showPanel("CHANGE_PHONE")));
        grid.add(tile("🔄", "Switch Acc",   new Color(2, 132, 199),  e -> onSwitchAccount()));
        grid.add(tile("🚪", "Logout",       Theme.DANGER,   this::onLogout));

        return grid;
    }

    private JPanel tile(String emoji, String label, Color color,
                        java.awt.event.ActionListener action) {
        JPanel tile = new JPanel(new BorderLayout(0, 8));
        tile.setBackground(Theme.CARD_BG);
        tile.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER_COLOR),
                new EmptyBorder(18, 12, 18, 12)));
        tile.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel emojiLbl = new JLabel(emoji, SwingConstants.CENTER);
        emojiLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 26));

        JLabel nameLbl = new JLabel(label, SwingConstants.CENTER);
        nameLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        nameLbl.setForeground(color);

        tile.add(emojiLbl, BorderLayout.CENTER);
        tile.add(nameLbl,  BorderLayout.SOUTH);

        // Make the whole tile clickable
        tile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                action.actionPerformed(new ActionEvent(tile,
                        ActionEvent.ACTION_PERFORMED, ""));
            }
            public void mouseEntered(java.awt.event.MouseEvent e) {
                tile.setBackground(Theme.PRIMARY_LIGHT);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                tile.setBackground(Theme.CARD_BG);
            }
        });
        return tile;
    }

    // ── Actions ───────────────────────────────────────────────────────────

    private void showBalance() {
        double bal = service.getBalance(loggedInAccountNo);
        Theme.showInfo(this, String.format("Current Balance: ₹%.2f", bal));
        refreshBalance();
    }

    private void onLogout(ActionEvent e) {
        if (Theme.showConfirm(this, "Are you sure you want to logout?")) {
            Theme.showInfo(this, "You have been logged out successfully.");
            mainFrame.logout();
        }
    }

    private void onSwitchAccount() {
        if (Theme.showConfirm(this, "Switch to a different account?\nYou will be logged out first.")) {
            mainFrame.logout();
        }
    }

    // ── Public API ────────────────────────────────────────────────────────

    /**
     * Called by MainFrame after a successful login to initialise the dashboard.
     */
    public void initForAccount(String accountNumber) {
        this.loggedInAccountNo = accountNumber;
        Account acc = service.getAccount(accountNumber);
        if (acc != null) {
            lblWelcome.setText("Welcome, " + acc.getFullName());
            lblAccNo.setText("Account: " + acc.getAccountNumber()
                           + "   |   Member since: " + acc.getCreatedAt().toLocalDate());
            refreshBalance();
        }
    }

    public void refreshBalance() {
        double bal = service.getBalance(loggedInAccountNo);
        lblBalance.setText(String.format("₹ %.2f", bal));
    }

    public String getLoggedInAccountNo() {
        return loggedInAccountNo;
    }
}
