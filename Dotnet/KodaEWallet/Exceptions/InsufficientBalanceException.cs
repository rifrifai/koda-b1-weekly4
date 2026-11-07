using KodaEWallet.Utils;

namespace KodaEWallet.Exceptions;

public class InsufficientBalanceException : WalletException
{
    public InsufficientBalanceException(long balance, long amount)
        : base($"Saldo tidak cukup. Saldo Anda {Format.Rupiah(balance)}, nominal transfer {Format.Rupiah(amount)}")
    {
    }
}
