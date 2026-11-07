package util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

// Format tampilan: rupiah dan tanggal
public class Format {

    // 500000 -> Rp500.000
    public static String rupiah(long amount) {
        return "Rp" + String.format(Locale.US, "%,d", amount).replace(",", ".");
    }

    // 2026-11-01T19:20 -> 01-11-2026 19:20
    public static String dateTime(LocalDateTime time) {
        return time.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
    }
}
