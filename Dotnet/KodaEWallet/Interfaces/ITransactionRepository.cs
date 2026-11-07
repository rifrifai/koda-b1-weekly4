using KodaEWallet.Models;

namespace KodaEWallet.Interfaces;

public interface ITransactionRepository
{
    int NextId();
    void Add(Transaction transaction);
    List<Transaction> FindByWallet(string walletNumber);
}
