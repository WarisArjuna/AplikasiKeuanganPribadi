package util;

import javax.swing.JButton;

public class ButtonStyler {

    // === PRIMARY (HIJAU) ===
    public static void stylePrimary(JButton btn) {
        btn.putClientProperty("FlatLaf.style",
            "background:#4CAF50; " +
            "foreground:#ffffff; " +
            "arc:12; " +
            "focusWidth:1;"
        );
        btn.putClientProperty("JButton.hoverBackground", "#43A047");
    }

    // === WARNING (KUNING) ===
    public static void styleWarning(JButton btn) {
        btn.putClientProperty("FlatLaf.style",
            "background:#FFC107; " +
            "foreground:#000000; " +
            "arc:12; " +
            "focusWidth:1;"
        );
        btn.putClientProperty("JButton.hoverBackground", "#FFB300");
    }

    // === DANGER (MERAH) ===
    public static void styleDanger(JButton btn) {
        btn.putClientProperty("FlatLaf.style",
            "background:#F44336; " +
            "foreground:#ffffff; " +
            "arc:12; " +
            "focusWidth:1;"
        );
        btn.putClientProperty("JButton.hoverBackground", "#E53935");
    }

    // === SECONDARY (ABU PREMIUM) ===
    public static void styleSecondary(JButton btn) {
        btn.putClientProperty("FlatLaf.style",
            "background:#E0E0E0; " +
            "foreground:#333333; " +
            "arc:12;"
        );
        btn.putClientProperty("JButton.hoverBackground", "#CCCCCC");
    }

    // === SIDEBAR BUTTON ===
    public enum SidebarColor {
        BLUE("#3498db", "#2980b9"),
        PURPLE("#9b59b6", "#8e44ad"),
        RED("#e74c3c", "#c0392b"),
        DARK("#2c3e50", "#34495e"),
        GREEN("#2ecc71", "#27ae60"),
        ORANGE("#e67e22", "#d35400");

        public final String bg;
        public final String hover;

        SidebarColor(String bg, String hover) {
            this.bg = bg;
            this.hover = hover;
        }
    }
    
    public static void styleSidebar(JButton btn, SidebarColor color) {
        btn.putClientProperty("FlatLaf.style",
            "background:" + color.bg + "; foreground:#ffffff; arc:10;");
        btn.putClientProperty("JButton.hoverBackground", color.hover);
    }



    // === NAVBAR BUTTON ===
    public static void styleNavbar(JButton btn) {
        btn.putClientProperty("FlatLaf.style",
            "background:#ffffff; " +
            "foreground:#333333; " +
            "arc:10;"
        );
        btn.putClientProperty("JButton.hoverBackground", "#F2F2F2");
    }
}
