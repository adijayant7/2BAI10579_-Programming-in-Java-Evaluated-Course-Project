package banking.ui;

import banking.model.Transaction;
import banking.service.BankingService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Transaction history with three filter tabs:
 *   All Transactions | Deposits | Withdrawals
 */
public class TransactionPanel extends JPanel {

    private final BankingService  service;
    private final MainFrame       mainFrame;
    private final DefaultTableModel tableModel;
    private final JTable          table;
    private final JLabel          lblCount;

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("dd MMM yyyy  HH:mm:ss");

    private static final String[] COLUMNS = {
        "#", "Date & Time", "Type", "Amount (₹)", "Balance After (₹)", "Description"
    };

    public TransactionPanel(BankingService service, MainFrame mainFrame) {
        this.service   = service;
        this.mainFrame = mainFrame;

        setLayout(new BorderLayout(0, 0));
        setBackground(Theme.BG);

        // ── Header ────────────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.PRIMARY);
        header.setBorder(new EmptyBorder(14, 24, 14, 24));

        JLabel title = new JLabel("📋  Transaction History");
        title.setFont(Theme.FONT_HEADING);
        title.setForeground(Color.WHITE);

        JButton btnBack = new JButton("← Dashboard");
        btnBack.setFont(Theme.FONT_BODY);
        btnBack.setForeground(Theme.PRIMARY);
        btnBack.setBackground(Color.WHITE);
        btnBack.setFocusPainted(false);
        btnBack.addActionListener(e -> mainFrame.showPanel("DASHBOARD"));

        header.add(title,   BorderLayout.WEST);
        header.add(btnBack, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // ── Tab bar ───────────────────────────────────────────────────────
        JPanel tabBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        tabBar.setBackground(Theme.BG);

        JButton btnAll  = Theme.primaryButton("All Transactions");
        JButton btnDep  = Theme.successButton("Deposits");
        JButton btnWith = Theme.dangerButton("Withdrawals");

        btnAll .setPreferredSize(new Dimension(160, 36));
        btnDep .setPreferredSize(new Dimension(120, 36));
        btnWith.setPreferredSize(new Dimension(140, 36));

        btnAll .addActionListener(e -> loadTransactions("ALL"));
        btnDep .addActionListener(e -> loadTransactions("DEPOSIT"));
        btnWith.addActionListener(e -> loadTransactions("WITHDRAWAL"));

        lblCount = new JLabel();
        lblCount.setFont(Theme.FONT_BODY);
        lblCount.setForeground(Theme.TEXT_GRAY);

        tabBar.add(btnAll);
        tabBar.add(btnDep);
        tabBar.add(btnWith);
        tabBar.add(lblCount);

        add(tabBar, BorderLayout.NORTH); // will add below header via wrapper

        // ── Table ─────────────────────────────────────────────────────────
        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(tableModel);
        table.setFont(Theme.FONT_BODY);
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(Theme.PRIMARY);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(Theme.PRIMARY_LIGHT);
        table.setGridColor(Theme.BORDER_COLOR);
        table.setShowGrid(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Color-code DEPOSIT / WITHDRAWAL rows
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    String type = (String) tableModel.getValueAt(row, 2);
                    if ("DEPOSIT".equals(type))
                        setBackground(new Color(240, 253, 244));
                    else if ("WITHDRAWAL".equals(type))
                        setBackground(new Color(255, 241, 242));
                    else
                        setBackground(Color.WHITE);
                }
                setBorder(new EmptyBorder(0, 8, 0, 8));
                return this;
            }
        });

        // Column widths
        table.getColumnModel().getColumn(0).setMaxWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(170);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(110);
        table.getColumnModel().getColumn(4).setPreferredWidth(130);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER_COLOR));

        // Layout the tabBar + table in CENTER
        JPanel centerPane = new JPanel(new BorderLayout());
        centerPane.setBackground(Theme.BG);
        centerPane.setBorder(new EmptyBorder(0, 20, 20, 20));
        centerPane.add(tabBar, BorderLayout.NORTH);
        centerPane.add(scroll, BorderLayout.CENTER);

        // Replace NORTH with just the header; CENTER gets everything else
        removeAll();
        add(header,    BorderLayout.NORTH);
        add(centerPane, BorderLayout.CENTER);
    }

    // ── Public API ────────────────────────────────────────────────────────

    /**
     * (Re)loads the table for the given filter when the panel becomes visible.
     */
    public void loadTransactions(String filter) {
        tableModel.setRowCount(0);
        String accNo = mainFrame.getLoggedInAccountNo();
        List<Transaction> list;

        switch (filter) {
            case "DEPOSIT":    list = service.getDeposits(accNo);     break;
            case "WITHDRAWAL": list = service.getWithdrawals(accNo);  break;
            default:           list = service.getAllTransactions(accNo); break;
        }

        int i = 1;
        for (Transaction t : list) {
            tableModel.addRow(new Object[]{
                i++,
                t.getTransactionDate().format(FMT),
                t.getType().name(),
                String.format("%.2f", t.getAmount()),
                String.format("%.2f", t.getBalanceAfter()),
                t.getDescription()
            });
        }

        String label = filter.equals("ALL") ? "All" :
                       filter.equals("DEPOSIT") ? "Deposits" : "Withdrawals";
        lblCount.setText("Showing " + list.size() + " " + label);
    }
}
