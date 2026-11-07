package exception;

// Nomor wallet tujuan tidak ada
public class WalletNotFoundException extends WalletException {
    public WalletNotFoundException(String walletNumber) {
        super("Wallet dengan nomor " + walletNumber + " tidak ditemukan");
    }
}
