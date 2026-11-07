package repository;

import interfaces.TransactionRepository;
import model.Transaction;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// Menyimpan riwayat transaksi di memori
public class InMemoryTransactionRepository implements TransactionRepository {
    private List<Transaction> transactions = new ArrayList<>();

    @Override
    public int nextId() {
        return transactions.size() + 1;
    }

    @Override
    public void add(Transaction transaction) {
        transactions.add(transaction);
    }

    // LAMBDA & STREAM: filter transaksi milik wallet tertentu
    @Override
    public List<Transaction> findByWallet(String walletNumber) {
        return transactions.stream()
                .filter(t -> walletNumber.equals(t.getSenderNumber())
                        || walletNumber.equals(t.getReceiverNumber()))
                .collect(Collectors.toList());
    }
}
