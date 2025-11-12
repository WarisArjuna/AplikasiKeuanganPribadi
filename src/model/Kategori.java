package model;

/**
 * Model class untuk menyimpan data kategori transaksi.
 * Bisa digunakan untuk mengelompokkan pemasukan/pengeluaran.
 */
public class Kategori {
    private int id;
    private String nama;
    private String jenis; // "Pemasukan" atau "Pengeluaran"

    // Constructor kosong
    public Kategori() {}

    // Constructor lengkap
    public Kategori(int id, String nama, String jenis) {
        this.id = id;
        this.nama = nama;
        this.jenis = jenis;
    }

    // Getter dan Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    // Menampilkan informasi kategori (opsional)
    @Override
    public String toString() {
        return nama + " (" + jenis + ")";
    }
}
