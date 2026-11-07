package repository;

import interfaces.WalletRepository;
import model.User;
import model.Wallet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// Menyimpan wallet di memori. COLLECTIONS & GENERICS: List<Wallet>
public class InMemoryWalletRepository implements WalletRepository {
    private List<Wallet> wallets = new ArrayList<>();

    public InMemoryWalletRepository() {
        // 3 pengguna dummy, masing-masing punya 1 wallet (saldo awal 0, diisi oleh DummyData)
        wallets.add(new Wallet("WAL001", new User(1, "andi", "Andi", "andi@mail.com", "081111111111")));
        wallets.add(new Wallet("WAL002", new User(2, "budi", "Budi", "budi@mail.com", "082222222222")));
        wallets.add(new Wallet("WAL003", new User(3, "citra", "Citra", "citra@mail.com", "083333333333")));
    }

    @Override
    public List<Wallet> findAll() {
        return new ArrayList<>(wallets);
    }

    // LAMBDA & STREAM: cari wallet berdasarkan nomor (tidak peduli huruf besar/kecil)
    @Override
    public Optional<Wallet> findByNumber(String walletNumber) {
        return wallets.stream()
                .filter(w -> w.getWalletNumber().equalsIgnoreCase(walletNumber.trim()))
                .findFirst();
    }
}
