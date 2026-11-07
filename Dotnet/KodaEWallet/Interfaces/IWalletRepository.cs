using KodaEWallet.Models;

namespace KodaEWallet.Interfaces;

public interface IWalletRepository
{
    List<Wallet> FindAll();

    Wallet? FindByNumber(string walletNumber);
}
