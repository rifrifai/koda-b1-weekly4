using KodaEWallet.Interfaces;
using KodaEWallet.Models;

namespace KodaEWallet.Repositories;

// Mengisi riwayat transaksi dummy. Saldo dihitung dari transaksinya,
// jadi saldo selalu sama dengan total pemasukan dikurangi pengeluaran.
//   Andi  : +200.000 (top up) +300.000 (dari Citra)                   = Rp500.000
//   Budi  : +400.000 (top up) -50.000 (ke Citra)                      = Rp350.000
//   Citra : +600.000 (top up) -300.000 (ke Andi) +50.000 (dari Budi)  = Rp350.000
public static class DummyData
{
    public static void Seed(IWalletRepository walletRepo, ITransactionRepository trxRepo)
    {
        TopUp(walletRepo, trxRepo, "WAL001", 200000, "Top up awal", new DateTime(2026, 10, 28, 9, 0, 0));
        TopUp(walletRepo, trxRepo, "WAL002", 400000, "Top up awal", new DateTime(2026, 10, 28, 10, 30, 0));
        TopUp(walletRepo, trxRepo, "WAL003", 600000, "Top up awal", new DateTime(2026, 10, 29, 8, 15, 0));
        Transfer(walletRepo, trxRepo, "WAL003", "WAL001", 300000, "Patungan makan", new DateTime(2026, 10, 30, 13, 45, 0));
        Transfer(walletRepo, trxRepo, "WAL002", "WAL003", 50000, "Beli kopi", new DateTime(2026, 11, 1, 19, 20, 0));
    }

    private static void TopUp(IWalletRepository w, ITransactionRepository t, string number,
                            long amount, string notes, DateTime time)
    {
        w.FindByNumber(number)!.Deposit(amount);
        t.Add(new TopUpTransaction(t.NextId(), number, amount, notes, time));
    }

    private static void Transfer(IWalletRepository w, ITransactionRepository t, string from, string to,
                                long amount, string notes, DateTime time)
    {
        w.FindByNumber(from)!.Withdraw(amount);
        w.FindByNumber(to)!.Deposit(amount);
        t.Add(new TransferTransaction(t.NextId(), from, to, amount, notes, time));
    }
}
