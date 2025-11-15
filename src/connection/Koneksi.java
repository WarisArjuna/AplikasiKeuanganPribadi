package connection;

import java.sql.*;
import javax.swing.JOptionPane;

/**
 * Kelas untuk mengatur koneksi SQLite.
 * Termasuk auto-create database dan tabel kalau belum ada.
 */
public class Koneksi {

    private static final String DB_URL = "jdbc:sqlite:database_keuangan.db";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL);
            createTablesIfNotExists(conn); // Auto bikin tabel
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Gagal konek database: " + e.getMessage(),
                    "Error DB", JOptionPane.ERROR_MESSAGE);
        }
        return conn;
    }

    /**
     * Bikin tabel kalau belum ada. Ini aman dipanggil setiap connect.
     */
    private static void createTablesIfNotExists(Connection conn) {
        String createKategori =
                "CREATE TABLE IF NOT EXISTS kategori (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "nama_kategori TEXT NOT NULL UNIQUE" +
                        ")";

        String createTransaksi =
                "CREATE TABLE IF NOT EXISTS transaksi (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "tanggal TEXT NOT NULL," +
                        "jenis TEXT NOT NULL," +
                        "keterangan TEXT," +
                        "jumlah REAL NOT NULL," +
                        "kategori_id INTEGER NOT NULL," +
                        "FOREIGN KEY (kategori_id) REFERENCES kategori(id)" +
                        ")";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createKategori);
            stmt.execute(createTransaksi);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Gagal bikin tabel: " + e.getMessage(),
                    "Error DB", JOptionPane.ERROR_MESSAGE);
        }
    }
}
