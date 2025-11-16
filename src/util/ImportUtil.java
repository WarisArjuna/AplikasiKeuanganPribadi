package util;

import database.TransaksiDAO;
import model.Transaksi;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class ImportUtil {

    public static boolean importFile(File file, String format, TransaksiDAO dao) {
        try {
            List<Transaksi> list;

            switch (format) {
                case "csv" -> list = importCSV(file);
                case "txt" -> list = importTXT(file);
                case "json" -> list = importJSON(file);
                case "pdf" -> list = importFromPDF(file);
                default -> { return false; }
            }

            if (list == null || list.isEmpty()) {
                return false;
            }

            for (Transaksi t : list) {
                dao.insert(t);
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ======================
    // CSV
    // ======================
    private static List<Transaksi> importCSV(File file) throws Exception {
        List<Transaksi> list = new ArrayList<>();

        BufferedReader r = new BufferedReader(new FileReader(file));
        String line;
        r.readLine(); // skip header

        while ((line = r.readLine()) != null) {
            String[] s = line.split(",");

            list.add(new Transaksi(
                    s[1],
                    s[2],
                    s[3],
                    Double.parseDouble(s[4])
            ));
        }
        return list;
    }

    // ======================
    // TXT
    // ======================
    private static List<Transaksi> importTXT(File file) throws Exception {
        List<Transaksi> list = new ArrayList<>();

        BufferedReader r = new BufferedReader(new FileReader(file));
        String line;

        while ((line = r.readLine()) != null) {
            String[] s = line.split("\\|");

            list.add(new Transaksi(
                    s[1].trim(),
                    s[2].trim(),
                    s[3].trim(),
                    Double.parseDouble(s[4].trim())
            ));
        }
        return list;
    }

    // ======================
    // JSON
    // ======================
    private static List<Transaksi> importJSON(File file) throws Exception {
        List<Transaksi> list = new ArrayList<>();

        BufferedReader r = new BufferedReader(new FileReader(file));
        StringBuilder json = new StringBuilder();

        String line;
        while ((line = r.readLine()) != null) json.append(line);

        String j = json.toString()
                .replace("[", "")
                .replace("]", "");

        String[] blocks = j.split("\\},\\s*\\{");

        for (String block : blocks) {
            block = block.replace("{", "").replace("}", "");
            String[] rows = block.split(",");

            String tanggal = rows[1].split(":")[1].replace("\"", "").trim();
            String jenis = rows[2].split(":")[1].replace("\"", "").trim();
            String ket = rows[3].split(":")[1].replace("\"", "").trim();
            double jumlah = Double.parseDouble(rows[4].split(":")[1].trim());

            list.add(new Transaksi(tanggal, jenis, ket, jumlah));
        }

        return list;
    }

    // ======================
    // PDF
    // ======================
    public static List<Transaksi> importFromPDF(File file) {
        List<Transaksi> list = new ArrayList<>();

        try (PDDocument doc = PDDocument.load(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(doc);

            String[] lines = text.split("\n");

            boolean reading = false;

            for (String line : lines) {
                line = line.trim();

                // Detect table header
                if (line.startsWith("ID ")) {
                    reading = true;
                    continue;
                }

                if (!reading) continue;
                if (line.isEmpty()) continue;

                String[] parts = line.split("\\s+");

                if (parts.length < 5) continue;

                try {
                    int id = Integer.parseInt(parts[0]);
                    String tanggal = parts[1];
                    String jenis = parts[2];

                    // Keterangan bisa lebih dari 1 kata → ambil semua sebelum jumlah
                    StringBuilder ket = new StringBuilder();
                    for (int i = 3; i < parts.length - 1; i++) {
                        ket.append(parts[i]).append(" ");
                    }

                    ket = new StringBuilder(ket.toString().trim());

                    String jumlahStr = parts[parts.length - 1]
                            .replace(",", "") // 100,000.00 → 100000.00
                            .replace(" ", "");

                    double jumlah = Double.parseDouble(jumlahStr);

                    list.add(new Transaksi(id, tanggal, jenis, ket.toString(), jumlah));

                } catch (Exception ignore) {}
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal membaca PDF: " + e.getMessage());
        }

        return list;
    }
}
