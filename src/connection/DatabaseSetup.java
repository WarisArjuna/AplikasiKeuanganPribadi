package connection;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

/**
 * Class DatabaseSetup
 * ---------------------------------------
 * Bertugas untuk memastikan tabel utama sudah ada di database.
 * Jika belum, tabel akan dibuat otomatis saat aplikasi dijalankan.
 * ---------------------------------------
 */
public class DatabaseSetup {

    // Membuat tabel transaksi jika belum ada.
    public static void createTableIfNotExists() {
        // Query SQL untuk membuat tabel
        String sql = """
            CREATE TABLE IF NOT EXISTS transaksi (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                tanggal TEXT NOT NULL,
                jenis TEXT CHECK(jenis IN ('Pemasukan', 'Pengeluaran')) NOT NULL,
                keterangan TEXT NOT NULL,
                jumlah REAL CHECK(jumlah >= 0) NOT NULL
            );
        """;

        try (Connection conn = Koneksi.getConnection();
             Statement stmt = conn.createStatement()) {

            // Eksekusi perintah SQL
            stmt.execute(sql);
            System.out.println("✅ Tabel 'transaksi' siap digunakan!");

        } catch (SQLException e) {
            System.err.println("❌ Gagal membuat atau mengakses tabel: " + e.getMessage());
        }
    }

    /**
     * Mengecek apakah tabel sudah siap digunakan.
     * Biasanya dipanggil saat aplikasi pertama kali dijalankan.
     */
    public static void initializeDatabase() {
        System.out.println("🔧 Mengecek struktur database...");
        createTableIfNotExists();
    }
}
