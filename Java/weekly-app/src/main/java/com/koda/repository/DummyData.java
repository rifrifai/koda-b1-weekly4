package repository;

import exception.WalletException;
import interfaces.TransactionRepository;
import interfaces.WalletRepository;
import model.TopUpTransaction;
import model.TransferTransaction;
import model.Wallet;
import java.time.LocalDateTime;

// Mengisi riwayat transaksi dummy. Saldo dihitung dari transaksinya,
// jadi saldo selalu sama dengan total pemasukan dikurangi pengeluaran.
//   Andi  : +200.000 (top up) +300.000 (dari Citra)                   = Rp500.000
//   Budi  : +400.000 (top up) -50.000 (ke Citra)                      = Rp350.000
//   Citra : +600.000 (top up) -300.000 (ke Andi) +50.000 (dari Budi)  = Rp350.000
public class DummyData {

    public static void seed(WalletRepository walletRepo, TransactionRepository trxRepo) {
        try {
            topUp(walletRepo, trxRepo, "WAL001", 200000, "Top up awal", LocalDateTime.of(2026, 10, 28, 9, 0));
            topUp(walletRepo, trxRepo, "WAL002", 400000, "Top up awal", LocalDateTime.of(2026, 10, 28, 10, 30));
            topUp(walletRepo, trxRepo, "WAL003", 600000, "Top up awal", LocalDateTime.of(2026, 10, 29, 8, 15));
            transfer(walletRepo, trxRepo, "WAL003", "WAL001", 300000, "Patungan makan", LocalDateTime.of(2026, 10, 30, 13, 45));
            transfer(walletRepo, trxRepo, "WAL002", "WAL003", 50000, "Beli kopi", LocalDateTime.of(2026, 11, 1, 19, 20));
        } catch (WalletException e) {
            throw new IllegalStateException("Dummy data tidak valid: " + e.getMessage());
        }
    }

    private static void topUp(WalletRepository w, TransactionRepository t, String number,
                              long amount, String notes, LocalDateTime time) throws WalletException {
        Wallet wallet = w.findByNumber(number).get();
        wallet.deposit(amount);
        t.add(new TopUpTransaction(t.nextId(), number, amount, notes, time));
    }

    private static void transfer(WalletRepository w, TransactionRepository t, String from, String to,
                                 long amount, String notes, LocalDateTime time) throws WalletException {
        w.findByNumber(from).get().withdraw(amount);
        w.findByNumber(to).get().deposit(amount);
        t.add(new TransferTransaction(t.nextId(), from, to, amount, notes, time));
    }
}
