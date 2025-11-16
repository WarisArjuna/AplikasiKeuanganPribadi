package database;

import model.Transaksi;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TransaksiDAO {

    private static final Logger LOG = Logger.getLogger(TransaksiDAO.class.getName());
    
    public double getTotalPemasukan() {
        String sql = "SELECT SUM(jumlah) AS total FROM transaksi WHERE jenis = 'Pemasukan'";
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            return rs.getDouble("total");

        } catch (SQLException e) {
            return 0;
        }
    }   

    public double getTotalPengeluaran() {
        String sql = "SELECT SUM(jumlah) AS total FROM transaksi WHERE jenis = 'Pengeluaran'";
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            return rs.getDouble("total");

        } catch (SQLException e) {
            return 0;
        }
    }

    public double getBalance() {
        return getTotalPemasukan() - getTotalPengeluaran();
    }

    
    // INSERT
    public boolean insert(Transaksi t) {
        String sql = "INSERT INTO transaksi(tanggal, jenis, keterangan, jumlah) VALUES (?, ?, ?, ?)";

        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, t.getTanggal());
            ps.setString(2, t.getJenis());
            ps.setString(3, t.getKeterangan());
            ps.setDouble(4, t.getJumlah());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Gagal INSERT transaksi", e);
            return false;
        }
    }

    // UPDATE
    public boolean update(Transaksi t) {
        String sql = "UPDATE transaksi SET tanggal=?, jenis=?, keterangan=?, jumlah=? WHERE id=?";

        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, t.getTanggal());
            ps.setString(2, t.getJenis());
            ps.setString(3, t.getKeterangan());
            ps.setDouble(4, t.getJumlah());
            ps.setInt(5, t.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Gagal UPDATE transaksi", e);
            return false;
        }
    }

    // DELETE
    public boolean delete(int id) {
        String sql = "DELETE FROM transaksi WHERE id=?";

        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Gagal DELETE transaksi id=" + id, e);
            return false;
        }
    }
    
    // DELETE ALL DATA   
    public boolean deleteAll() {
        String sql = "DELETE FROM transaksi";

        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Gagal DELETE ALL transaksi", e);
            return false;
        }
    }

    
    // GET ALL
    public List<Transaksi> getAll() {
        List<Transaksi> list = new ArrayList<>();
        String sql = "SELECT * FROM transaksi ORDER BY tanggal DESC, id DESC";

        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Transaksi(
                        rs.getInt("id"),
                        rs.getString("tanggal"),
                        rs.getString("jenis"),
                        rs.getString("keterangan"),
                        rs.getDouble("jumlah")
                ));
            }

        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Gagal GET ALL transaksi", e);
        }
        return list;
    }

    // SEARCH
    public List<Transaksi> search(String keyword) {
        List<Transaksi> list = new ArrayList<>();
        String sql = "SELECT * FROM transaksi WHERE keterangan LIKE ? OR jenis LIKE ? ORDER BY tanggal DESC";

        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            String key = "%" + keyword + "%";
            ps.setString(1, key);
            ps.setString(2, key);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Transaksi(
                            rs.getInt("id"),
                            rs.getString("tanggal"),
                            rs.getString("jenis"),
                            rs.getString("keterangan"),
                            rs.getDouble("jumlah")
                    ));
                }
            }

        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Gagal SEARCH transaksi dengan keyword: " + keyword, e);
        }
        return list;
    }
}
