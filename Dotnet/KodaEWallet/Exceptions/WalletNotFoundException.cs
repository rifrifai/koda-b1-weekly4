namespace KodaEWallet.Exceptions;

public class WalletNotFoundException : WalletException
{
    public WalletNotFoundException(string walletNumber)
        : base($"Wallet dengan nomor {walletNumber} tidak ditemukan")
    {
    }
}
