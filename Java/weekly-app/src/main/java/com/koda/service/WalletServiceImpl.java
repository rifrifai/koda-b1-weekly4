package service;

import exception.InsufficientBalanceException;
import exception.InvalidAmountException;
import exception.SelfTransferException;
import exception.WalletException;
import exception.WalletNotFoundException;
import interfaces.TransactionRepository;
import interfaces.WalletRepository;
import interfaces.WalletService;
import model.HistoryFilter;
import model.TopUpTransaction;
import model.Transaction;
import model.TransferTransaction;
import model.Wallet;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

// Semua aturan bisnis e-wallet ada di sini (bukan di Main)
public class WalletServiceImpl implements WalletService {
    private WalletRepository walletRepo;
    private TransactionRepository trxRepo;

    // Repository diberikan dari luar lewat constructor
    public WalletServiceImpl(WalletRepository walletRepo, TransactionRepository trxRepo) {
        this.walletRepo = walletRepo;
        this.trxRepo = trxRepo;
    }

    @Override
    public List<Wallet> getAllWallets() {
        return walletRepo.findAll();
    }

    @Override
    public void validateAmount(long amount) throws WalletException {
        if (amount <= 0) {
            throw new InvalidAmountException();
        }
    }

    @Override
    public void topUp(Wallet wallet, long amount, String notes) throws WalletException {
        validateAmount(amount); // ditolak sebelum saldo disentuh
        wallet.deposit(amount);
        trxRepo.add(new TopUpTransaction(trxRepo.nextId(), wallet.getWalletNumber(),
                amount, notes, LocalDateTime.now()));
    }

    @Override
    public Wallet findReceiver(Wallet sender, String receiverNumber) throws WalletException {
        Wallet receiver = walletRepo.findByNumber(receiverNumber)
                .orElseThrow(() -> new WalletNotFoundException(receiverNumber));
        if (receiver.getWalletNumber().equals(sender.getWalletNumber())) {
            throw new SelfTransferException();
        }
        return receiver;
    }

    @Override
    public void validateTransfer(Wallet sender, String receiverNumber, long amount) throws WalletException {
        findReceiver(sender, receiverNumber);
        validateAmount(amount);
        if (amount > sender.getBalance()) {
            throw new InsufficientBalanceException(sender.getBalance(), amount);
        }
    }

    @Override
    public Transaction transfer(Wallet sender, String receiverNumber, long amount, String notes)
            throws WalletException {
        // 1) Cek SEMUA aturan dulu. Kalau ada yang gagal, berhenti di sini:
        //    saldo tidak berubah dan riwayat tidak bertambah.
        validateTransfer(sender, receiverNumber, amount);
        Wallet receiver = findReceiver(sender, receiverNumber);

        // 2) Baru ubah data (tidak mungkin gagal karena sudah divalidasi)
        sender.withdraw(amount);
        receiver.deposit(amount);
        Transaction trx = new TransferTransaction(trxRepo.nextId(), sender.getWalletNumber(),
                receiver.getWalletNumber(), amount, notes, LocalDateTime.now());
        trxRepo.add(trx);
        return trx;
    }

    // LAMBDA & STREAM: filter (semua/pemasukan/pengeluaran) + urutkan terbaru di atas
    @Override
    public List<Transaction> getHistory(Wallet wallet, HistoryFilter filter) {
        String number = wallet.getWalletNumber();
        return trxRepo.findByWallet(number).stream()
                .filter(t -> filter == HistoryFilter.ALL
                        || (filter == HistoryFilter.INCOME && t.isIncomeFor(number))
                        || (filter == HistoryFilter.EXPENSE && !t.isIncomeFor(number)))
                .sorted((a, b) -> {
                    int byTime = b.getCreatedAt().compareTo(a.getCreatedAt());
                    if (byTime != 0) {
                        return byTime;
                    }
                    return b.getId() - a.getId();
                })
                .collect(Collectors.toList());
    }

    // STREAM: jumlahkan nominal
    @Override
    public long getTotalIncome(Wallet wallet) {
        String number = wallet.getWalletNumber();
        return trxRepo.findByWallet(number).stream()
                .filter(t -> t.isIncomeFor(number))
                .mapToLong(t -> t.getAmount())
                .sum();
    }

    @Override
    public long getTotalExpense(Wallet wallet) {
        String number = wallet.getWalletNumber();
        return trxRepo.findByWallet(number).stream()
                .filter(t -> !t.isIncomeFor(number))
                .mapToLong(t -> t.getAmount())
                .sum();
    }
}
