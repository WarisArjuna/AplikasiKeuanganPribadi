package controller;

import com.toedter.calendar.JDateChooser;
import database.TransaksiDAO;
import model.Transaksi;
import util.ExportUtil;
import util.ImportUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import util.InputValidator;

public class KeuanganController {

    private final JPanel pnlKeuangan;

    private final JTable tableTransaksi;
    private final JDateChooser dateTanggal;
    private final JComboBox<String> cmbJenis;
    private final JTextField txtKeterangan;
    private final JTextField txtJumlah;
    private final JTextField txtSearch;
    
    private DashboardController dashboard;
    private final JButton btnSimpan;
    private final JButton btnEdit;
    private final JButton btnHapus;
    private final JButton btnRefresh;

    // NEW
    private final JButton btnExport;
    private final JButton btnImport;
    private JLabel lblStatus;

    private final TransaksiDAO dao = new TransaksiDAO();
    private Integer selectedId = null;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private final NumberFormat nf = NumberFormat.getNumberInstance(new Locale("id", "ID"));

    public KeuanganController(
            JPanel pnlKeuangan,
            JTable tableTransaksi,
            JDateChooser dateTanggal,
            JComboBox<String> cmbJenis,
            JTextField txtKeterangan,
            JTextField txtJumlah,
            JTextField txtSearch,
            JButton btnSimpan,
            JButton btnEdit,
            JButton btnHapus,
            JButton btnRefresh,
            JButton btnExport,
            JButton btnImport,
            JLabel lblStatus,
            DashboardController dashboard 
    ) {
        this.pnlKeuangan = pnlKeuangan;
        this.tableTransaksi = tableTransaksi;
        this.dateTanggal = dateTanggal;
        this.cmbJenis = cmbJenis;
        this.txtKeterangan = txtKeterangan;
        this.txtJumlah = txtJumlah;
        this.txtSearch = txtSearch;
        this.btnSimpan = btnSimpan;
        this.btnEdit = btnEdit;
        this.btnHapus = btnHapus;
        this.btnRefresh = btnRefresh;
        this.btnExport = btnExport;
        this.btnImport = btnImport;
        this.lblStatus = lblStatus;
        this.dashboard = dashboard;
        
        init();
    }

    private void init() {
        formatJumlahInput();
        loadTable();
        setupListeners();
    }

    private void setStatus(String msg) {
        if (lblStatus != null) {
            lblStatus.setText(msg);
        }
    }

    // ==========================================================
    // FORMAT INPUT txtJumlah
    // ==========================================================
    private void formatJumlahInput() {

        ((AbstractDocument) txtJumlah.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {

                if (text.matches("[0-9]*")) {
                    super.replace(fb, offset, length, text, attrs);
                    formatField(fb);
                }
            }

            @Override
            public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr)
                    throws BadLocationException {

                if (text.matches("[0-9]*")) {
                    super.insertString(fb, offset, text, attr);
                    formatField(fb);
                }
            }

            private void formatField(FilterBypass fb) throws BadLocationException {
                String value = fb.getDocument().getText(0, fb.getDocument().getLength());

                if (value.isEmpty()) return;

                try {
                    long number = Long.parseLong(value.replace(".", "").replace(",", ""));
                    String formatted = nf.format(number);
                    fb.replace(0, fb.getDocument().getLength(), formatted, null);
                } catch (NumberFormatException | BadLocationException e) {
                    // ignore
                }
            }
        });
    }

    private double parseRupiah(String text) {
        try {
            return nf.parse(text).doubleValue();
        } catch (ParseException e) {
            return 0;
        }
    }

    // ==========================================================
    // LOAD TABLE
    // ==========================================================
    public void loadTable() {
        List<Transaksi> list = dao.getAll();

        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"ID", "Tanggal", "Jenis", "Keterangan", "Jumlah"}, 0
        );

        for (Transaksi t : list) {
            model.addRow(new Object[]{
                    t.getId(),
                    t.getTanggal(),
                    t.getJenis(),
                    t.getKeterangan(),
                    nf.format(t.getJumlah())
            });
        }

        tableTransaksi.setModel(model);
    }

    // ==========================================================
    // LISTENER
    // ==========================================================
    private void setupListeners() {

        tableTransaksi.getSelectionModel().addListSelectionListener(e -> {
            if (!tableTransaksi.getSelectionModel().isSelectionEmpty()) {
                int row = tableTransaksi.getSelectedRow();

                selectedId = Integer.valueOf(tableTransaksi.getValueAt(row, 0).toString());

                try {
                    java.util.Date d = sdf.parse(tableTransaksi.getValueAt(row, 1).toString());
                    dateTanggal.setDate(d);
                } catch (ParseException ex) {
                    dateTanggal.setDate(null);
                }

                cmbJenis.setSelectedItem(tableTransaksi.getValueAt(row, 2));
                txtKeterangan.setText(tableTransaksi.getValueAt(row, 3).toString());
                txtJumlah.setText(tableTransaksi.getValueAt(row, 4).toString());
            }
        });

        btnSimpan.addActionListener(e -> save());
        btnEdit.addActionListener(e -> update());
        btnRefresh.addActionListener(e -> resetForm());
        btnHapus.addActionListener(e -> {
            // Popup dengan 3 opsi
            String[] options = {"Hapus Pilihan", "Hapus Semua Data", "Cancel"};

            int pilihan = JOptionPane.showOptionDialog(
                    pnlKeuangan,
                    "Pilih tindakan yang ingin dilakukan:",
                    "Konfirmasi Hapus",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            switch (pilihan) {

                case 0: // HAPUS PILIHAN
                    hapusPilihan();
                    break;

                case 1: // HAPUS SEMUA DATA
                    hapusSemua();
                    break;

                default: // CANCEL

                    break;
            }
        });

        
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                search(txtSearch.getText());
            }
        });

        // NEW
        btnExport.addActionListener(e -> exportData());
        btnImport.addActionListener(e -> importData());
    }

    // ==========================================================
    // INSERT
    // ==========================================================
    private void save() {

        if (!InputValidator.validateTanggal(dateTanggal.getDate())) return;
        if (!InputValidator.validateJenis(cmbJenis.getSelectedItem().toString())) return;
        if (!InputValidator.validateJumlah(txtJumlah.getText())) return;

        Transaksi t = new Transaksi();
        t.setTanggal(sdf.format(dateTanggal.getDate()));
        t.setJenis(cmbJenis.getSelectedItem().toString());
        t.setKeterangan(txtKeterangan.getText());

        double jumlah = Double.parseDouble(txtJumlah.getText().replace(".", "").replace(",", "."));
        t.setJumlah(jumlah);

        if (dao.insert(t)) {
            JOptionPane.showMessageDialog(null, "Berhasil ditambahkan!");
            resetForm();
            loadTable();
        } else {
            JOptionPane.showMessageDialog(null, "Gagal menambah data!");
        }
    }
    
    // ==========================================================
    // UPDATE
    // ==========================================================
    private void update() {
        if (selectedId == null) {
            JOptionPane.showMessageDialog(null, "Pilih data dulu!");
            return;
        }

        Transaksi t = new Transaksi(
                selectedId,
                sdf.format(dateTanggal.getDate()),
                cmbJenis.getSelectedItem().toString(),
                txtKeterangan.getText(),
                parseRupiah(txtJumlah.getText())
        );

        if (dao.update(t)) {
            setStatus("Berhasil diupdate");
            loadTable();
            // refresh card dashboard
            if (dashboard != null) {
                dashboard.loadData();
            }
        }
    }

    // ==========================================================
    // DELETE
    // ==========================================================
    private void hapusPilihan() {
        int row = tableTransaksi.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(pnlKeuangan, 
                "Tidak ada baris yang dipilih.",
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (int) tableTransaksi.getValueAt(row, 0);

        if (dao.delete(id)) {
            JOptionPane.showMessageDialog(pnlKeuangan,
                "Data berhasil dihapus.",
                "Sukses",
                JOptionPane.INFORMATION_MESSAGE);
            loadTable();
        } else {
            JOptionPane.showMessageDialog(pnlKeuangan,
                "Gagal menghapus data.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
        // refresh card dashboard
        if (dashboard != null) {
            dashboard.loadData();
        }
    }

    private void hapusSemua() {
        int konf = JOptionPane.showConfirmDialog(
                pnlKeuangan,
                "Yakin ingin menghapus SEMUA data?",
                "Konfirmasi",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (konf == JOptionPane.YES_OPTION) {

            if (dao.deleteAll()) {
                JOptionPane.showMessageDialog(pnlKeuangan,
                    "Semua data berhasil dihapus.",
                    "Sukses",
                    JOptionPane.INFORMATION_MESSAGE);
                loadTable();
            } else {
                JOptionPane.showMessageDialog(pnlKeuangan,
                    "Gagal menghapus semua data.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
        // refresh card dashboard
        if (dashboard != null) {
            dashboard.loadData();
        }
    }

    
    // ==========================================================
    // SEARCH
    // ==========================================================
    private void search(String key) {
        List<Transaksi> list = dao.search(key);

        DefaultTableModel model = (DefaultTableModel) tableTransaksi.getModel();
        model.setRowCount(0);

        for (Transaksi t : list) {
            model.addRow(new Object[]{
                    t.getId(),
                    t.getTanggal(),
                    t.getJenis(),
                    t.getKeterangan(),
                    nf.format(t.getJumlah())
            });
        }
        // refresh card dashboard
        if (dashboard != null) {
            dashboard.loadData();
        }
    }

    // ==========================================================
    // RESET FORM
    // ==========================================================
    private void resetForm() {
        selectedId = null;
        dateTanggal.setDate(null);
        cmbJenis.setSelectedIndex(0);
        txtKeterangan.setText("");
        txtJumlah.setText("");
        txtSearch.setText("");

        tableTransaksi.clearSelection();
        loadTable();
        // refresh card dashboard
        if (dashboard != null) {
            dashboard.loadData();
        }
    }

    // ==========================================================
    // EXPORT DATA
    // ==========================================================
    private void exportData() {
        String[] opsi = {"CSV", "TXT", "JSON", "PDF"};
        String format = (String) JOptionPane.showInputDialog(
                null,
                "Pilih format file:",
                "Export",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opsi,
                opsi[0]
        );

        if (format == null) {
            setStatus("Export dibatalkan");
            return;
        }

        // FORMAT NAMA FILE
        String waktu = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new java.util.Date());
        String namaFile = "keuangan-" + waktu + "." + format.toLowerCase();

        // BUAT FOLDER EXPORT
        File folder = new File("export_keuangan");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // LOKASI FILE OTOMATIS
        File file = new File(folder, namaFile);

        // AMBIL DATA
        List<Transaksi> data = dao.getAll();

        // EXPORT
        boolean ok = ExportUtil.export(data, file, format);

        setStatus(ok ? "Export " + format + " berhasil → " + file.getAbsolutePath()
                     : "Export gagal");
    }


    // ==========================================================
    // IMPORT DATA
    // ==========================================================
    private void importData() {

        JFileChooser fc = new JFileChooser();
        int res = fc.showOpenDialog(null);

        if (res != JFileChooser.APPROVE_OPTION) {
            setStatus("Import dibatalkan");
            return;
        }

        File file = fc.getSelectedFile();
        String format = getExtension(file.getName());

        boolean ok = ImportUtil.importFile(file, format, dao);

        if (ok) {
            setStatus("Import " + format + " berhasil");
            loadTable();
        } else {
            setStatus("Import gagal");
        }
    }

    private String getExtension(String name) {
        if (!name.contains(".")) return "";
        return name.substring(name.lastIndexOf(".") + 1).toLowerCase();
    }
}
