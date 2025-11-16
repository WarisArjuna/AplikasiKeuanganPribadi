package controller;

import database.TransaksiDAO;
import javax.swing.JLabel;
import java.text.NumberFormat;
import java.util.Locale;

public class DashboardController {

    private final JLabel lblIncomeValue;
    private final JLabel lblPengeluaranValue;
    private final JLabel lblBalanceValue;

    private final TransaksiDAO dao = new TransaksiDAO();
    private final NumberFormat nf = NumberFormat.getNumberInstance(new Locale("id", "ID"));

    public DashboardController(JLabel pemasukan, JLabel pengeluaran, JLabel balance) {
        this.lblIncomeValue = pemasukan;
        this.lblPengeluaranValue = pengeluaran;
        this.lblBalanceValue = balance;

        loadData();
    }

    public final void loadData() {
        double pemasukan = dao.getTotalPemasukan();
        double pengeluaran = dao.getTotalPengeluaran();
        double balance = pemasukan - pengeluaran;

        lblIncomeValue.setText("Rp " + nf.format(pemasukan));
        lblPengeluaranValue.setText("Rp " + nf.format(pengeluaran));
        lblBalanceValue.setText("Rp " + nf.format(balance));
    }
}
