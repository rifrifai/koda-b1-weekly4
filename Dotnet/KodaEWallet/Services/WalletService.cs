using KodaEWallet.Exceptions;
using KodaEWallet.Interfaces;
using KodaEWallet.Models;

namespace KodaEWallet.Services;

public class WalletService : IWalletService
{
    private readonly IWalletRepository _walletRepo;
    private readonly ITransactionRepository _trxRepo;
    public WalletService(IWalletRepository walletRepo, ITransactionRepository trxRepo)
    {
        _walletRepo = walletRepo;
        _trxRepo = trxRepo;
    }

    public List<Wallet> GetAllWallets() => _walletRepo.FindAll();
    public void ValidateAmount(long amount)
    {
        if (amount <= 0)
            throw new InvalidAmountException();
    }

    public void TopUp(Wallet wallet, long amount, string notes)
    {
        ValidateAmount(amount);

        wallet.Deposit(amount);
        _trxRepo.Add(new TopUpTransaction(_trxRepo.NextId(), wallet.WalletNumber,
            amount, notes, DateTime.Now));
    }

    public Wallet FindReceiver(Wallet sender, string receiverNumber)
    {
        Wallet? receiver = _walletRepo.FindByNumber(receiverNumber);
        if (receiver == null)
            throw new WalletNotFoundException(receiverNumber);
        if (receiver.WalletNumber == sender.WalletNumber)
            throw new SelfTransferException();

        return receiver;
    }

    public void ValidateTransfer(Wallet sender, string receiverNumber, long amount)
    {
        FindReceiver(sender, receiverNumber);
        ValidateAmount(amount);
        if (amount > sender.Balance)
            throw new InsufficientBalanceException(sender.Balance, amount);
    }

    public Transaction Transfer(Wallet sender, string receiverNumber, long amount, string notes)
    {
        ValidateTransfer(sender, receiverNumber, amount);
        Wallet receiver = FindReceiver(sender, receiverNumber);

        sender.Withdraw(amount);
        receiver.Deposit(amount);
        Transaction trx = new TransferTransaction(_trxRepo.NextId(), sender.WalletNumber,
            receiver.WalletNumber, amount, notes, DateTime.Now);
        _trxRepo.Add(trx);
        return trx;
    }

    public List<Transaction> GetHistory(Wallet wallet, HistoryFilter filter)
    {
        string number = wallet.WalletNumber;
        return _trxRepo.FindByWallet(number)
            .Where(t => filter == HistoryFilter.All || (filter == HistoryFilter.Income && t.IsIncomeFor(number)) || (filter == HistoryFilter.Expense && !t.IsIncomeFor(number)))
            .OrderByDescending(t => t.CreatedAt)
            .ThenByDescending(t => t.Id)
            .ToList();
    }

    public long GetTotalIncome(Wallet wallet)
    {
        string number = wallet.WalletNumber;
        return _trxRepo.FindByWallet(number)
            .Where(t => t.IsIncomeFor(number))
            .Sum(t => t.Amount);
    }

    public long GetTotalExpense(Wallet wallet)
    {
        string number = wallet.WalletNumber;
        return _trxRepo.FindByWallet(number)
            .Where(t => !t.IsIncomeFor(number))
            .Sum(t => t.Amount);
    }
}
