using KodaEWallet.Exceptions;

namespace KodaEWallet.Models;

public class Wallet
{
    public string WalletNumber { get; }
    public User Owner { get; }
    public long Balance { get; private set; }

    public Wallet(string walletNumber, User owner)
    {
        WalletNumber = walletNumber;
        Owner = owner;
        Balance = 0;
    }

    public void Deposit(long amount)
    {
        if (amount <= 0)
            throw new InvalidAmountException();

        Balance += amount;
    }

    public void Withdraw(long amount)
    {
        if (amount <= 0)
            throw new InvalidAmountException();
        if (amount > Balance)
            throw new InsufficientBalanceException(Balance, amount);

        Balance -= amount;
    }
}
