using System.Globalization;

namespace KodaEWallet.Utils;

public static class Format
{
    public static string Rupiah(long amount) =>
        "Rp" + amount.ToString("N0", CultureInfo.InvariantCulture).Replace(",", ".");

    public static string DateTimeText(DateTime time) =>
        time.ToString("dd-MM-yyyy HH:mm", CultureInfo.InvariantCulture);
}
