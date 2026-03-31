package banking.ui;

import banking.db.DBConnection;
import banking.service.BankingService;

import javax.swing.*;
import java.awt.*;

/**
 * Root application window.  Uses CardLayout to switch between screens.
 */
public class MainFrame extends JFrame {

    // ── Panel keys ────────────────────────────────────────────────────────
    public static final String LOGIN           = "LOGIN";
    public static final String CREATE_ACCOUNT  = "CREATE_ACCOUNT";
    public static final String DASHBOARD       = "DASHBOARD";
    public static final String DEPOSIT         = "DEPOSIT";
    public static final String WITHDRAW        = "WITHDRAW";
    public static final String TRANSACTIONS    = "TRANSACTIONS";
    public static final String CHANGE_PASSWORD = "CHANGE_PASSWORD";
    public static final String CHANGE_PHONE    = "CHANGE_PHONE";

    // ── State ─────────────────────────────────────────────────────────────
    private String loggedInAccountNo = null;

    // ── Layout ────────────────────────────────────────────────────────────
    private final CardLayout      cardLayout = new CardLayout();
    private final JPanel          cardPanel  = new JPanel(cardLayout);

    // ── Panels ────────────────────────────────────────────────────────────
    private final LoginPanel          loginPanel;
    private final CreateAccountPanel  createAccountPanel;
    private final DashboardPanel      dashboardPanel;
    private final DepositPanel        depositPanel;
    private final WithdrawPanel       withdrawPanel;
    private final TransactionPanel    transactionPanel;
    private final ChangePasswordPanel changePasswordPanel;
    private final ChangePhonePanel    changePhonePanel;

    public MainFrame() {
        super("BankingSystem");

        BankingService service = new BankingService();

        // Build all panels
        loginPanel          = new LoginPanel(service, this);
        createAccountPanel  = new CreateAccountPanel(service, this);
        dashboardPanel      = new DashboardPanel(service, this);
        depositPanel        = new DepositPanel(service, this);
        withdrawPanel       = new WithdrawPanel(service, this);
        transactionPanel    = new TransactionPanel(service, this);
        changePasswordPanel = new ChangePasswordPanel(service, this);
        changePhonePanel    = new ChangePhonePanel(service, this);

        // Register all panels
        cardPanel.add(loginPanel,          LOGIN);
        cardPanel.add(createAccountPanel,  CREATE_ACCOUNT);
        cardPanel.add(dashboardPanel,      DASHBOARD);
        cardPanel.add(depositPanel,        DEPOSIT);
        cardPanel.add(withdrawPanel,       WITHDRAW);
        cardPanel.add(transactionPanel,    TRANSACTIONS);
        cardPanel.add(changePasswordPanel, CHANGE_PASSWORD);
        cardPanel.add(changePhonePanel,    CHANGE_PHONE);

        setContentPane(cardPanel);
        cardLayout.show(cardPanel, LOGIN);

        // Frame settings
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                onExit();
            }
        });

        setSize(900, 640);
        setMinimumSize(new Dimension(760, 540));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ── Navigation ────────────────────────────────────────────────────────

    /**
     * Switches to the named panel.  Performs panel-specific pre-show work.
     */
    public void showPanel(String name) {
        switch (name) {
            case TRANSACTIONS:
                transactionPanel.loadTransactions("ALL");
                break;
            case LOGIN:
                loginPanel.reset();
                break;
            default:
                break;
        }
        cardLayout.show(cardPanel, name);
    }

    // ── Auth helpers ──────────────────────────────────────────────────────

    /** Called by LoginPanel on successful authentication. */
    public void loginSuccess(String accountNumber) {
        this.loggedInAccountNo = accountNumber;
        dashboardPanel.initForAccount(accountNumber);
        cardLayout.show(cardPanel, DASHBOARD);
    }

    /** Logs the user out and returns to the login screen. */
    public void logout() {
        loggedInAccountNo = null;
        loginPanel.reset();
        cardLayout.show(cardPanel, LOGIN);
    }

    // ── Accessors used by child panels ────────────────────────────────────

    public String          getLoggedInAccountNo() { return loggedInAccountNo; }
    public DashboardPanel  getDashboard()          { return dashboardPanel; }

    // ── Exit ──────────────────────────────────────────────────────────────

    private void onExit() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to exit BankingSystem?",
                "Exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (choice == JOptionPane.YES_OPTION) {
            DBConnection.closeConnection();
            dispose();
            System.exit(0);
        }
    }
}
