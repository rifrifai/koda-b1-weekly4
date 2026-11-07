using KodaEWallet.Interfaces;
using KodaEWallet.Models;

namespace KodaEWallet.Repositories;

public class InMemoryWalletRepository : IWalletRepository
{
    private readonly List<Wallet> _wallets = [];
    public InMemoryWalletRepository()
    {
        _wallets.Add(new Wallet("WAL001", new User(1, "andi", "Andi", "andi@mail.com", "081111111111")));
        _wallets.Add(new Wallet("WAL002", new User(2, "budi", "Budi", "budi@mail.com", "082222222222")));
        _wallets.Add(new Wallet("WAL003", new User(3, "citra", "Citra", "citra@mail.com", "083333333333")));
    }

    public List<Wallet> FindAll() => new List<Wallet>(_wallets);

    public Wallet? FindByNumber(string walletNumber) =>
        _wallets.FirstOrDefault(w => w.WalletNumber.Equals(walletNumber.Trim(), StringComparison.OrdinalIgnoreCase));
}
