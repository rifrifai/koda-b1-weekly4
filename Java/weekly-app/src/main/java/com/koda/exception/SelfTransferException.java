package exception;

// Transfer ke wallet sendiri
public class SelfTransferException extends WalletException {
    public SelfTransferException() {
        super("Tidak bisa transfer ke wallet sendiri");
    }
}
