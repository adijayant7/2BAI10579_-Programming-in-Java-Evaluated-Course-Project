package banking.ui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Central place for colours, fonts, and helper factory methods.
 * Change values here to retheme the entire application.
 */
public final class Theme {

    // ── Brand palette ─────────────────────────────────────────────────────
    public static final Color PRIMARY        = new Color(26,  86, 219);   // deep blue
    public static final Color PRIMARY_DARK   = new Color(14,  60, 160);
    public static final Color PRIMARY_LIGHT  = new Color(219, 234, 254);
    public static final Color ACCENT         = new Color(16, 185, 129);   // emerald
    public static final Color DANGER         = new Color(220,  38,  38);
    public static final Color WARNING        = new Color(245, 158,  11);
    public static final Color SUCCESS        = new Color(16,  185, 129);

    public static final Color BG             = new Color(245, 247, 250);
    public static final Color CARD_BG        = Color.WHITE;
    public static final Color TEXT_DARK      = new Color(15,  23,  42);
    public static final Color TEXT_GRAY      = new Color(100, 116, 139);
    public static final Color BORDER_COLOR   = new Color(203, 213, 225);
    public static final Color INPUT_BG       = new Color(248, 250, 252);

    // ── Fonts ─────────────────────────────────────────────────────────────
    public static final Font FONT_TITLE   = new Font("Segoe UI", Font.BOLD,  22);
    public static final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD,  16);
    public static final Font FONT_LABEL   = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BODY    = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_SMALL   = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_MONO    = new Font("Courier New", Font.PLAIN, 12);

    private Theme() {}

    // ── Factory helpers ───────────────────────────────────────────────────

    /** A primary-coloured rounded button. */
    public static JButton primaryButton(String text) {
        JButton btn = roundedButton(text, PRIMARY, Color.WHITE);
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(PRIMARY_DARK); }
            public void mouseExited (java.awt.event.MouseEvent e) { btn.setBackground(PRIMARY); }
        });
        return btn;
    }

    /** A danger (red) rounded button. */
    public static JButton dangerButton(String text) {
        return roundedButton(text, DANGER, Color.WHITE);
    }

    /** A success (green) rounded button. */
    public static JButton successButton(String text) {
        return roundedButton(text, ACCENT, Color.WHITE);
    }

    /** A secondary (outline-style) button. */
    public static JButton secondaryButton(String text) {
        JButton btn = roundedButton(text, Color.WHITE, PRIMARY);
        btn.setBorder(BorderFactory.createLineBorder(PRIMARY, 1));
        return btn;
    }

    private static JButton roundedButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(180, 40));
        return btn;
    }

    /** Styled text field. */
    public static JTextField textField(int columns) {
        JTextField tf = new JTextField(columns);
        tf.setFont(FONT_LABEL);
        tf.setBackground(INPUT_BG);
        tf.setForeground(TEXT_DARK);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                new EmptyBorder(6, 10, 6, 10)));
        return tf;
    }

    /** Styled password field. */
    public static JPasswordField passwordField(int columns) {
        JPasswordField pf = new JPasswordField(columns);
        pf.setFont(FONT_LABEL);
        pf.setBackground(INPUT_BG);
        pf.setForeground(TEXT_DARK);
        pf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                new EmptyBorder(6, 10, 6, 10)));
        return pf;
    }

    /** A label used for form field captions. */
    public static JLabel fieldLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(TEXT_DARK);
        return lbl;
    }

    /** Card-style panel with a white background and a subtle shadow border. */
    public static JPanel card() {
        JPanel panel = new JPanel();
        panel.setBackground(CARD_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                new EmptyBorder(20, 24, 20, 24)));
        return panel;
    }

    /** Shows an info-style alert dialog. */
    public static void showInfo(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Information",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /** Shows a success-style alert dialog. */
    public static void showSuccess(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Success ✓",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /** Shows an error alert dialog. */
    public static void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    /** Shows a warning alert dialog. */
    public static void showWarning(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Warning",
                JOptionPane.WARNING_MESSAGE);
    }

    /** Shows a confirm dialog; returns true if user clicks OK/Yes. */
    public static boolean showConfirm(Component parent, String message) {
        return JOptionPane.showConfirmDialog(parent, message, "Confirm",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)
               == JOptionPane.YES_OPTION;
    }

    /** Applies the application-wide look-and-feel. */
    public static void applyLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { /* fall back to default */ }
    }
}
