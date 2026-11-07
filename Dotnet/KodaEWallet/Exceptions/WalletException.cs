namespace KodaEWallet.Exceptions;

public class WalletException : Exception
{
    public WalletException(string message) : base(message) { }
}
