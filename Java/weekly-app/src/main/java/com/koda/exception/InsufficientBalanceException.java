package exception;

import util.Format;

// Saldo tidak cukup untuk transfer
public class InsufficientBalanceException extends WalletException {
    public InsufficientBalanceException(long balance, long amount) {
        super("Saldo tidak cukup. Saldo Anda " + Format.rupiah(balance)
                + ", nominal transfer " + Format.rupiah(amount));
    }
}
