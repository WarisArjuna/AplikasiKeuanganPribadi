package database;

import java.sql.*;
import javax.swing.JOptionPane;

// Kelas untuk koneksi SQLite (auto-create database & tabel).
public class Koneksi {

    private static final String DB_URL = "jdbc:sqlite:database_keuangan.db";

    public static Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            createTablesIfNotExists(conn);
            return conn;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Gagal konek database: " + e.getMessage(),
                    "Error DB", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    // Membuat tabel jika belum ada — aman dipanggil setiap connect.

    private static void createTablesIfNotExists(Connection conn) {

        String createTransaksi =
                "CREATE TABLE IF NOT EXISTS transaksi (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "tanggal TEXT NOT NULL," +
                        "jenis TEXT CHECK(jenis IN ('Pemasukan','Pengeluaran')) NOT NULL," +
                        "keterangan TEXT NOT NULL," +
                        "jumlah REAL CHECK(jumlah >= 0) NOT NULL" +
                        ")";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createTransaksi);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Gagal membuat tabel: " + e.getMessage(),
                    "Error DB", JOptionPane.ERROR_MESSAGE);
        }
    }
}
