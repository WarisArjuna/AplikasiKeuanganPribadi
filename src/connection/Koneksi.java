package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class Koneksi
 * ---------------------------------------
 * Mengatur koneksi ke database SQLite.
 * File database (keuangan.db) otomatis dibuat jika belum ada.
 * ---------------------------------------
 */
public class Koneksi {

    // Menyimpan objek koneksi agar tidak dibuat berulang kali
    private static Connection conn;

    /**
     * Method untuk mendapatkan koneksi ke database SQLite.
     * @return objek Connection ke database
     */
    public static Connection getConnection() {
        if (conn == null) {
            try {
                // Lokasi database (dibuat otomatis di root project)
                String url = "jdbc:sqlite:keuangan.db";

                // Membuat koneksi
                conn = DriverManager.getConnection(url);

                // Validasi koneksi
                if (conn != null) {
                    System.out.println("✅ Koneksi ke database SQLite berhasil!");
                }

            } catch (SQLException e) {
                System.err.println("❌ Gagal membuat koneksi ke database: " + e.getMessage());
            }
        }
        return conn;
    }

    // Menutup koneksi database secara aman.

    public static void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
                conn = null;
                System.out.println("🔒 Koneksi database ditutup.");
            } catch (SQLException e) {
                System.err.println("⚠️ Gagal menutup koneksi: " + e.getMessage());
            }
        }
    }
}
