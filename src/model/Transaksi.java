package model;

/**
 * Model untuk data transaksi
 */
public class Transaksi {
    private int id;
    private String tanggal;
    private String jenis;
    private String keterangan;
    private double jumlah;
    private int kategoriId;

    public Transaksi() {}

    // ✅ Constructor lengkap (buat SELECT dari database)
    public Transaksi(int id, String tanggal, String jenis, String keterangan, double jumlah, int kategoriId) {
        this.id = id;
        this.tanggal = tanggal;
        this.jenis = jenis;
        this.keterangan = keterangan;
        this.jumlah = jumlah;
        this.kategoriId = kategoriId;
    }

    // ✅ Constructor singkat (buat INSERT baru)
    public Transaksi(String tanggal, String jenis, String keterangan, double jumlah, int kategoriId) {
        this.tanggal = tanggal;
        this.jenis = jenis;
        this.keterangan = keterangan;
        this.jumlah = jumlah;
        this.kategoriId = kategoriId;
    }

    // Getter dan Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public double getJumlah() {
        return jumlah;
    }

    public void setJumlah(double jumlah) {
        this.jumlah = jumlah;
    }

    public int getKategoriId() {
        return kategoriId;
    }

    public void setKategoriId(int kategoriId) {
        this.kategoriId = kategoriId;
    }
}
