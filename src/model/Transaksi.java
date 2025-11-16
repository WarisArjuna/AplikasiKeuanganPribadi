package model;

public class Transaksi {
    private Integer id;
    private String tanggal;      // format yyyy-MM-dd
    private String jenis;        // "Pemasukan" atau "Pengeluaran"
    private String keterangan;
    private Double jumlah;

    // === CONSTRUCTOR KOSONG ===
    public Transaksi() {
    }

    // === CONSTRUCTOR UTAMA (ID) ===
    public Transaksi(Integer id, String tanggal, String jenis, String keterangan, Double jumlah) {
        this.id = id;
        this.tanggal = tanggal;
        this.jenis = jenis;
        this.keterangan = keterangan;
        this.jumlah = jumlah;
    }

    // === CONSTRUCTOR TANPA ID ===
    public Transaksi(String tanggal, String jenis, String keterangan, Double jumlah) {
        this.tanggal = tanggal;
        this.jenis = jenis;
        this.keterangan = keterangan;
        this.jumlah = jumlah;
    }

    // === GETTER & SETTER ===
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Double getJumlah() {
        return jumlah;
    }

    public void setJumlah(Double jumlah) {
        this.jumlah = jumlah;
    }

    // === toString buat debug gampang ===
    @Override
    public String toString() {
        return "Transaksi{" +
                "id=" + id +
                ", tanggal='" + tanggal + '\'' +
                ", jenis='" + jenis + '\'' +
                ", keterangan='" + keterangan + '\'' +
                ", jumlah=" + jumlah +
                '}';
    }
}
