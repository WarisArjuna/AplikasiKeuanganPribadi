package controller;

import connection.Koneksi;
import model.Kategori;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Controller untuk mengelola data kategori (CRUD)
 * Menerapkan validasi input dan error handling agar aplikasi stabil.
 */
public class KategoriController {

    /**
     * Menambahkan kategori baru ke database.
     * @param kategori objek kategori yang ingin ditambahkan
     * @return true jika berhasil, false jika gagal
     */
    public boolean tambahKategori(Kategori kategori) {
        // Validasi input
        if (kategori.getNamaKategori() == null || kategori.getNamaKategori().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Nama kategori tidak boleh kosong!",
                    "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        String sql = "INSERT INTO kategori (nama_kategori) VALUES (?)";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, kategori.getNamaKategori());
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Gagal menambah kategori: " + e.getMessage(),
                    "Error Database", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Mengambil semua data kategori dari database.
     * @return List berisi semua kategori.
     */
    public List<Kategori> getAllKategori() {
        List<Kategori> list = new ArrayList<>();
        String sql = "SELECT * FROM kategori ORDER BY nama_kategori ASC";

        try (Connection conn = Koneksi.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Kategori k = new Kategori(
                        rs.getInt("id"),
                        rs.getString("nama_kategori")
                );
                list.add(k);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Gagal mengambil data kategori: " + e.getMessage(),
                    "Error Database", JOptionPane.ERROR_MESSAGE);
        }

        return list;
    }

    /**
     * Mengupdate data kategori berdasarkan ID.
     * @param kategori objek kategori yang akan diperbarui
     * @return true jika berhasil, false jika gagal
     */
    public boolean updateKategori(Kategori kategori) {
        if (kategori.getId() <= 0) {
            JOptionPane.showMessageDialog(null,
                    "ID kategori tidak valid!",
                    "Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (kategori.getNamaKategori() == null || kategori.getNamaKategori().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Nama kategori tidak boleh kosong!",
                    "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        String sql = "UPDATE kategori SET nama_kategori=? WHERE id=?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, kategori.getNamaKategori());
            stmt.setInt(2, kategori.getId());

            int updated = stmt.executeUpdate();
            return updated > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Gagal mengupdate kategori: " + e.getMessage(),
                    "Error Database", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Menghapus kategori berdasarkan ID.
     * @param id ID kategori yang akan dihapus
     * @return true jika berhasil, false jika gagal
     */
    public boolean hapusKategori(int id) {
        if (id <= 0) {
            JOptionPane.showMessageDialog(null,
                    "ID kategori tidak valid!",
                    "Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        String sql = "DELETE FROM kategori WHERE id=?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int deleted = stmt.executeUpdate();

            if (deleted == 0) {
                JOptionPane.showMessageDialog(null,
                        "Kategori tidak ditemukan atau sudah dihapus.",
                        "Informasi", JOptionPane.INFORMATION_MESSAGE);
            }

            return deleted > 0;

        } catch (SQLException e) {
            // Tangani jika kategori masih digunakan di tabel transaksi
            if (e.getMessage().contains("foreign key")) {
                JOptionPane.showMessageDialog(null,
                        "Tidak bisa menghapus kategori yang masih digunakan dalam transaksi!",
                        "Error Relasi", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                        "Gagal menghapus kategori: " + e.getMessage(),
                        "Error Database", JOptionPane.ERROR_MESSAGE);
            }
            return false;
        }
    }
}
