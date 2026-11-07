package interfaces;

import exception.WalletException;
import model.HistoryFilter;
import model.Transaction;
import model.Wallet;
import java.util.List;

// INTERFACE: daftar fitur e-wallet yang dipakai oleh Main
public interface WalletService {
    List<Wallet> getAllWallets();

    // Nominal harus lebih dari 0
    void validateAmount(long amount) throws WalletException;

    void topUp(Wallet wallet, long amount, String notes) throws WalletException;

    // Cari penerima. Gagal jika tidak ditemukan atau sama dengan pengirim.
    Wallet findReceiver(Wallet sender, String receiverNumber) throws WalletException;

    // Cek semua aturan transfer TANPA mengubah data
    void validateTransfer(Wallet sender, String receiverNumber, long amount) throws WalletException;

    Transaction transfer(Wallet sender, String receiverNumber, long amount, String notes) throws WalletException;

    List<Transaction> getHistory(Wallet wallet, HistoryFilter filter);

    long getTotalIncome(Wallet wallet);

    long getTotalExpense(Wallet wallet);
}
