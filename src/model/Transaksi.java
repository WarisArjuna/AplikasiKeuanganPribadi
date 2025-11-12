package model;

/**
 * Class Transaksi
 * -----------------------------------------
 * Representasi satu data transaksi keuangan.
 * Atribut utama:
 *  - id         : Nomor unik transaksi
 *  - tanggal    : Tanggal transaksi (format: yyyy-MM-dd)
 *  - jenis      : Jenis transaksi (Pemasukan / Pengeluaran)
 *  - keterangan : Deskripsi singkat transaksi
 *  - jumlah     : Nominal uang transaksi
 * -----------------------------------------
 * Class ini digunakan sebagai model data (POJO)
 * untuk berkomunikasi dengan database SQLite.
 */
public class Transaksi {

    // ==========================
    // Atribut / Properti
    // ==========================
    private int id;
    private String tanggal;
    private String jenis;
    private String keterangan;
    private double jumlah;
    private int kategoriId;

    // ==========================
    // Konstruktor
    // ==========================

    /**
     * Konstruktor tanpa parameter (default)
     * Digunakan saat membuat objek kosong.
     */
    public Transaksi() {
    }

    // Konstruktor lengkap (biasanya dipakai saat ambil data dari database)
    public Transaksi(int id, String tanggal, String jenis, String keterangan, double jumlah) {
        this.id = id;
        this.tanggal = tanggal;
        this.jenis = jenis;
        this.keterangan = keterangan;
        this.jumlah = jumlah;
    }

    // Konstruktor tanpa ID (biasanya untuk transaksi baru)

    public Transaksi(String tanggal, String jenis, String keterangan, double jumlah) {
        this.tanggal = tanggal;
        this.jenis = jenis;
        this.keterangan = keterangan;
        this.jumlah = jumlah;
    }

    // ==========================
    // Getter & Setter
    // ==========================

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
        // Validasi kecil biar jenis cuma boleh dua jenis
        if (jenis.equalsIgnoreCase("Pemasukan") || jenis.equalsIgnoreCase("Pengeluaran")) {
            this.jenis = jenis;
        } else {
            throw new IllegalArgumentException("Jenis transaksi harus 'Pemasukan' atau 'Pengeluaran'");
        }
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
        if (jumlah < 0) {
            throw new IllegalArgumentException("Jumlah transaksi tidak boleh negatif!");
        }
        this.jumlah = jumlah;
    }

    public int getKategoriId() {
        return kategoriId;
    }

    public void setKategoriId(int kategoriId) {
        this.kategoriId = kategoriId;
    }
    
    

    // ==========================
    // Method Tambahan
    // ==========================

    // Mengubah objek transaksi menjadi format string rapi.Berguna untuk debugging atau export ke file.
    @Override
    public String toString() {
        return String.format("ID: %d | %s | %s | %s | Rp%.2f",
                id, tanggal, jenis, keterangan, jumlah);
    }
}
