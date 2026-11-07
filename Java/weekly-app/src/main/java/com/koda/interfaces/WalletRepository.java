package interfaces;

import model.Wallet;
import java.util.List;
import java.util.Optional;

// INTERFACE: janji tempat menyimpan data wallet.
// Sekarang disimpan di memori (dummy data), nanti bisa diganti database tanpa mengubah service.
public interface WalletRepository {
    List<Wallet> findAll();

    Optional<Wallet> findByNumber(String walletNumber);
}
