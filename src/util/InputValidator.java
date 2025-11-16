package util;

import javax.swing.*;
import java.util.Date;

public class InputValidator {

    public static boolean validateTanggal(Date date) {
        if (date == null) {
            JOptionPane.showMessageDialog(null, "Tanggal tidak boleh kosong!");
            return false;
        }
        return true;
    }

    public static boolean validateJenis(String jenis) {
        if (jenis == null || jenis.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Pilih jenis transaksi!");
            return false;
        }
        return true;
    }

    public static boolean validateJumlah(String jumlahStr) {
        if (jumlahStr == null || jumlahStr.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Jumlah tidak boleh kosong!");
            return false;
        }

        try {
            double val = Double.parseDouble(jumlahStr.replace(".", "").replace(",", "."));
            if (val <= 0) {
                JOptionPane.showMessageDialog(null, "Jumlah harus lebih dari 0!");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Jumlah harus berupa angka!");
            return false;
        }

        return true;
    }
}
