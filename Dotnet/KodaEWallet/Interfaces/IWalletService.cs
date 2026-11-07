using KodaEWallet.Models;

namespace KodaEWallet.Interfaces;

public interface IWalletService
{
    List<Wallet> GetAllWallets();
    void ValidateAmount(long amount);
    void TopUp(Wallet wallet, long amount, string notes);
    Wallet FindReceiver(Wallet sender, string receiverNumber);
    void ValidateTransfer(Wallet sender, string receiverNumber, long amount);
    Transaction Transfer(Wallet sender, string receiverNumber, long amount, string notes);
    List<Transaction> GetHistory(Wallet wallet, HistoryFilter filter);
    long GetTotalIncome(Wallet wallet);
    long GetTotalExpense(Wallet wallet);
}
