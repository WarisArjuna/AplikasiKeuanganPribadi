package controller;

import connection.Koneksi;
import model.Transaksi;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Controller untuk mengelola data transaksi (CRUD)
 * Dilengkapi validasi dan error handling agar lebih aman.
 */
public class TransaksiController {

    /**
     * Menyimpan transaksi baru ke database.
     * @param transaksi objek Transaksi yang berisi data transaksi baru.
     * @return true jika berhasil, false jika gagal.
     */
    public boolean tambahTransaksi(Transaksi transaksi) {
        // Validasi data input
        if (transaksi.getTanggal() == null || transaksi.getTanggal().trim().isEmpty() ||
            transaksi.getJenis() == null || transaksi.getJenis().trim().isEmpty() ||
            transaksi.getKeterangan() == null || transaksi.getKeterangan().trim().isEmpty() ||
            transaksi.getJumlah() <= 0) {
            JOptionPane.showMessageDialog(null, 
                    "Data transaksi tidak boleh kosong dan jumlah harus lebih dari 0!", 
                    "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        String sql = "INSERT INTO transaksi (tanggal, jenis, keterangan, jumlah, kategori_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, transaksi.getTanggal());
            stmt.setString(2, transaksi.getJenis());
            stmt.setString(3, transaksi.getKeterangan());
            stmt.setDouble(4, transaksi.getJumlah());
            stmt.setInt(5, transaksi.getKategoriId());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                    "Gagal menambah transaksi: " + e.getMessage(), 
                    "Error Database", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Mengambil semua data transaksi dari database.
     * @return List berisi semua transaksi.
     */
    public List<Transaksi> getAllTransaksi() {
        List<Transaksi> list = new ArrayList<>();
        String sql = "SELECT * FROM transaksi ORDER BY tanggal DESC";

        try (Connection conn = Koneksi.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Transaksi t = new Transaksi(
                        rs.getInt("id"),
                        rs.getString("tanggal"),
                        rs.getString("jenis"),
                        rs.getString("keterangan"),
                        rs.getDouble("jumlah"),
                        rs.getInt("kategori_id")
                );
                list.add(t);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                    "Gagal mengambil data transaksi: " + e.getMessage(), 
                    "Error Database", JOptionPane.ERROR_MESSAGE);
        }
        return list;
    }

    /**
     * Mengupdate data transaksi berdasarkan ID.
     */
    public boolean updateTransaksi(Transaksi transaksi) {
        if (transaksi.getId() <= 0) {
            JOptionPane.showMessageDialog(null, "ID transaksi tidak valid!", "Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        String sql = "UPDATE transaksi SET tanggal=?, jenis=?, keterangan=?, jumlah=?, kategori_id=? WHERE id=?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, transaksi.getTanggal());
            stmt.setString(2, transaksi.getJenis());
            stmt.setString(3, transaksi.getKeterangan());
            stmt.setDouble(4, transaksi.getJumlah());
            stmt.setInt(5, transaksi.getKategoriId());
            stmt.setInt(6, transaksi.getId());

            int updated = stmt.executeUpdate();
            return updated > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                    "Gagal mengupdate transaksi: " + e.getMessage(), 
                    "Error Database", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Menghapus transaksi berdasarkan ID.
     */
    public boolean hapusTransaksi(int id) {
        if (id <= 0) {
            JOptionPane.showMessageDialog(null, "ID tidak valid!", "Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        String sql = "DELETE FROM transaksi WHERE id=?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int deleted = stmt.executeUpdate();
            return deleted > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                    "Gagal menghapus transaksi: " + e.getMessage(), 
                    "Error Database", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
