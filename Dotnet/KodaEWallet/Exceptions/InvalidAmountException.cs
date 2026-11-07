namespace KodaEWallet.Exceptions;

public class InvalidAmountException : WalletException
{
    public InvalidAmountException() : base("Nominal harus lebih dari 0") { }
}
