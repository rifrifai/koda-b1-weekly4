using KodaEWallet.Interfaces;
using KodaEWallet.Models;

namespace KodaEWallet.Repositories;

public class InMemoryTransactionRepository : ITransactionRepository
{
    private readonly List<Transaction> _transactions = [];
    public int NextId() => _transactions.Count + 1;
    public void Add(Transaction transaction) => _transactions.Add(transaction);
    public List<Transaction> FindByWallet(string walletNumber) =>
        _transactions
            .Where(t => t.SenderNumber == walletNumber || t.ReceiverNumber == walletNumber)
            .ToList();
}
