package interfaces;

import model.Transaction;
import java.util.List;

// INTERFACE: janji tempat menyimpan riwayat transaksi
public interface TransactionRepository {
    int nextId();

    void add(Transaction transaction);

    // Semua transaksi yang melibatkan wallet ini (sebagai pengirim atau penerima)
    List<Transaction> findByWallet(String walletNumber);
}
