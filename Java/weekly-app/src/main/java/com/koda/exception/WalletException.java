package exception;

// Induk semua exception di aplikasi ini.
// Dengan satu induk, Main cukup menulis satu catch untuk semua kesalahan transaksi.
public class WalletException extends Exception {
    public WalletException(String message) {
        super(message);
    }
}
