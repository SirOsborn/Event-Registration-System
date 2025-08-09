package com.eventms.view.ui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.JTextComponent;

/**
 * Centralized UI theme for a modern, cohesive 2025 look across the app.
 * Provides color tokens, font helpers, and component styling utilities.
 */
public final class Theme {
    private Theme() {}

    // Cyan combination minimal theme
    public static final Color BRAND_PRIMARY = new Color(6, 182, 212);   // Cyan 500 #06B6D4
    public static final Color BRAND_SUCCESS = new Color(22, 163, 74);   // Green 600 #16A34A
    public static final Color BRAND_INFO    = new Color(14, 165, 233);  // Sky 500 #0EA5E9
    public static final Color BRAND_WARNING = new Color(245, 158, 11);  // Amber 500 #F59E0B
    public static final Color BRAND_DANGER  = new Color(225, 29, 72);   // Rose 600 #E11D48

    public static final Color TEXT_PRIMARY   = new Color(15, 23, 42);    // Slate-900 #0F172A
    public static final Color TEXT_SECONDARY = new Color(71, 85, 105);   // Slate-600 #475569
    public static final Color BG_APP         = new Color(240, 249, 255); // Sky-50 #F0F9FF
    public static final Color BG_SURFACE     = Color.WHITE;               // Clean surface
    public static final Color BORDER         = new Color(226, 232, 240); // Slate-200 #E2E8F0

    // Preferred font stack on Windows; falls back gracefully
    private static final String[] FONT_STACK = new String[]{
            "Segoe UI", "Inter", "SF Pro Text", "Roboto", "Arial", "SansSerif"
    };

    public static void install() {
        // Set global fonts and base colors
        Font uiFont = fontPlain(14);
        UIManager.put("Label.font", uiFont);
        UIManager.put("Button.font", fontBold(14));
        UIManager.put("TextField.font", uiFont);
        UIManager.put("PasswordField.font", uiFont);
        UIManager.put("ComboBox.font", uiFont);
        UIManager.put("TextArea.font", uiFont);
        UIManager.put("CheckBox.font", uiFont);
        UIManager.put("Menu.font", uiFont);
        UIManager.put("MenuItem.font", uiFont);
        UIManager.put("TabbedPane.font", uiFont);

        UIManager.put("Panel.background", BG_APP);
        UIManager.put("OptionPane.background", BG_SURFACE);
        UIManager.put("OptionPane.messageFont", uiFont);
        UIManager.put("OptionPane.buttonFont", fontBold(13));
    }

    // Font helpers
    public static Font fontPlain(int size) { return new Font(pickFontName(), Font.PLAIN, size); }
    public static Font fontBold(int size)  { return new Font(pickFontName(), Font.BOLD, size); }
    public static Font fontSemiBold(int size) { return new Font(pickFontName(), Font.BOLD, size); }

    private static String pickFontName() {
        for (String f : FONT_STACK) {
            if (isFontAvailable(f)) return f;
        }
        return "SansSerif";
    }

    private static boolean isFontAvailable(String name) {
        for (Font f : GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts()) {
            if (f.getFamily().equalsIgnoreCase(name) || f.getName().equalsIgnoreCase(name)) return true;
        }
        return false;
    }

    // Component styling
    public static void stylePrimaryButton(JButton b) { styleButton(b, BRAND_PRIMARY); }
    public static void styleSuccessButton(JButton b) { styleButton(b, BRAND_SUCCESS); }
    public static void styleInfoButton(JButton b)    { styleButton(b, BRAND_INFO); }
    public static void styleDangerButton(JButton b)  { styleButton(b, BRAND_DANGER); }
    public static void styleSecondaryButton(JButton b) { styleButton(b, new Color(107, 114, 128)); } // Gray-500

    public static void styleButton(JButton b, Color bg) {
        b.setFont(fontBold(14));
        b.setForeground(Color.WHITE);
        b.setBackground(bg);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(true);
        b.setOpaque(true);
        b.setPreferredSize(new Dimension(140, 36));
        // Hover/press feedback
        Color hover = blend(bg, Color.BLACK, 0.08f);
        Color press = blend(bg, Color.BLACK, 0.16f);
        b.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { b.setBackground(hover); }
            @Override public void mouseExited(MouseEvent e)  { b.setBackground(bg); }
            @Override public void mousePressed(MouseEvent e) { b.setBackground(press); }
            @Override public void mouseReleased(MouseEvent e){ b.setBackground(hover); }
        });
    }

    public static void styleLinkButton(AbstractButton b) {
        b.setFont(fontPlain(12));
        b.setForeground(BRAND_PRIMARY);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public static void styleTextField(JTextComponent c) {
        c.setFont(fontPlain(14));
        // Subtle 1px border, rounded corners via empty border spacing
        c.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(230, 236, 244)), new EmptyBorder(6, 8, 6, 8)));
    c.setBackground(BG_SURFACE);
        c.setForeground(TEXT_PRIMARY);
        c.setCaretColor(TEXT_PRIMARY);
        if (c instanceof JPasswordField pf) {
            pf.setEchoChar('•');
        }
        // margin handled by EmptyBorder above
    }

    public static void styleComboBox(JComboBox<?> combo) {
        combo.setFont(fontPlain(14));
        combo.setBackground(BG_SURFACE);
        combo.setForeground(TEXT_PRIMARY);
    combo.setBorder(new LineBorder(new Color(230, 236, 244)));
    }

    public static void styleCheckBox(JCheckBox cb) {
        cb.setFont(fontPlain(14));
        cb.setForeground(TEXT_PRIMARY);
        cb.setBackground(BG_SURFACE);
    }

    public static void styleCard(JPanel p) {
        p.setBackground(BG_SURFACE);
        p.setBorder(new EmptyBorder(16, 16, 16, 16));
    }

    public static Border cardBorder() {
        return BorderFactory.createCompoundBorder(
                new LineBorder(BORDER),
                new EmptyBorder(12, 14, 12, 14)
        );
    }

    public static JLabel h1(String text) {
        JLabel l = new JLabel(text);
        l.setFont(fontBold(24));
        l.setForeground(BRAND_PRIMARY);
        return l;
    }

    public static JLabel subheading(String text) {
        JLabel l = new JLabel(text);
        l.setFont(fontPlain(15));
        l.setForeground(TEXT_SECONDARY);
        return l;
    }

    private static Color blend(Color c1, Color c2, float ratio) {
        float r = Math.min(1f, Math.max(0f, ratio));
        int red = (int)(c1.getRed() * (1 - r) + c2.getRed() * r);
        int green = (int)(c1.getGreen() * (1 - r) + c2.getGreen() * r);
        int blue = (int)(c1.getBlue() * (1 - r) + c2.getBlue() * r);
        return new Color(red, green, blue);
    }

    // App icon loader with classpath fallback
    private static Image cachedIcon;
    public static Image getAppIcon() {
        if (cachedIcon != null) return cachedIcon;
        try {
            java.net.URL url = Theme.class.getClassLoader().getResource("icon.png");
            if (url != null) {
                cachedIcon = new ImageIcon(url).getImage();
                return cachedIcon;
            }
        } catch (Exception ignore) {}
        try {
            // Fallback to project path during dev
            cachedIcon = Toolkit.getDefaultToolkit().createImage("src/main/resources/icon.png");
        } catch (Exception ignore) {}
        return cachedIcon;
    }
}
