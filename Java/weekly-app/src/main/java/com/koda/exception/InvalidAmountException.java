package exception;

// Nominal 0 atau negatif
public class InvalidAmountException extends WalletException {
    public InvalidAmountException() {
        super("Nominal harus lebih dari 0");
    }
}
