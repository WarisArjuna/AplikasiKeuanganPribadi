package util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import model.Transaksi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FileOutputStream;
import java.util.List;

public class ExportUtil {

    public static boolean export(List<Transaksi> data, File file, String format) {
        try {
            switch (format.toLowerCase()) {
                case "csv":
                    return exportCSV(data, file);

                case "txt":
                    return exportTXT(data, file);

                case "json":
                    return exportJSON(data, file);

                case "pdf":
                    return exportPDF(data, file);

                default:
                    return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ====================== PDF =========================

    private static boolean exportPDF(List<Transaksi> list, File file) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file));

            document.open();

            // Title
            Paragraph title = new Paragraph("LAPORAN TRANSAKSI\n\n",
                    new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD));
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Table
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);

            table.addCell("ID");
            table.addCell("Tanggal");
            table.addCell("Jenis");
            table.addCell("Keterangan");
            table.addCell("Jumlah");

            for (Transaksi t : list) {
                table.addCell(String.valueOf(t.getId()));
                table.addCell(t.getTanggal());
                table.addCell(t.getJenis());
                table.addCell(t.getKeterangan());
                table.addCell(String.format("%,.2f", t.getJumlah()));
            }

            document.add(table);
            document.close();

            return true;

        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ====================== CSV =========================
    private static boolean exportCSV(List<Transaksi> list, File file) throws Exception {
        FileWriter fw = new FileWriter(file);

        fw.write("id,tanggal,jenis,keterangan,jumlah\n");
        for (Transaksi t : list) {
            fw.write(t.getId() + "," +
                    t.getTanggal() + "," +
                    t.getJenis() + "," +
                    t.getKeterangan() + "," +
                    t.getJumlah() + "\n");
        }
        fw.close();
        return true;
    }

    // ====================== TXT =========================
    private static boolean exportTXT(List<Transaksi> list, File file) throws Exception {
        FileWriter fw = new FileWriter(file);

        for (Transaksi t : list) {
            fw.write("ID: " + t.getId() + "\n");
            fw.write("Tanggal: " + t.getTanggal() + "\n");
            fw.write("Jenis: " + t.getJenis() + "\n");
            fw.write("Keterangan: " + t.getKeterangan() + "\n");
            fw.write("Jumlah: " + t.getJumlah() + "\n");
            fw.write("-----------------------------\n");
        }

        fw.close();
        return true;
    }

    // ====================== JSON =========================
    private static boolean exportJSON(List<Transaksi> list, File file) throws Exception {
        FileWriter fw = new FileWriter(file);

        fw.write("[\n");
        for (int i = 0; i < list.size(); i++) {
            Transaksi t = list.get(i);

            fw.write("  {\n");
            fw.write("    \"id\": " + t.getId() + ",\n");
            fw.write("    \"tanggal\": \"" + t.getTanggal() + "\",\n");
            fw.write("    \"jenis\": \"" + t.getJenis() + "\",\n");
            fw.write("    \"keterangan\": \"" + t.getKeterangan() + "\",\n");
            fw.write("    \"jumlah\": " + t.getJumlah() + "\n");
            fw.write("  }");

            if (i < list.size() - 1) fw.write(",");
            fw.write("\n");
        }
        fw.write("]");

        fw.close();
        return true;
    }
}
