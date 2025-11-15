package controller;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JPanel;

public class SidebarController {

    private final JPanel pnlContent;  
    private final HashMap<JButton, String> menuMap = new HashMap<>();
    private JButton activeButton = null;

    public SidebarController(JPanel pnlContent) {
        this.pnlContent = pnlContent;
    }

    // Register menu button dengan nama card
    public void addMenu(JButton btn, String cardName) {
        menuMap.put(btn, cardName);

        // Klik event
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setActive(btn);
                showCard(cardName);
            }
        });
    }

    // Tampilkan panel via CardLayout
    private void showCard(String cardName) {
        CardLayout cl = (CardLayout) pnlContent.getLayout();
        cl.show(pnlContent, cardName);
    }

    // Menandai button aktif
    private void setActive(JButton btn) {
        activeButton = btn;
    }

    // Set default panel saat pertama kali program berjalan
    public void setDefault(JButton btn) {
        activeButton = btn;
        showCard(menuMap.get(btn));
    }

    // Tambahan opsional kalau lo mau ngecek button aktif dari luar
    public JButton getActiveButton() {
        return activeButton;
    }
}