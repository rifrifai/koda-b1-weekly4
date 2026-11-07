package com.koda;
import exception.WalletException;
import interfaces.TransactionRepository;
import interfaces.WalletRepository;
import interfaces.WalletService;
import model.HistoryFilter;
import model.Transaction;
import model.Wallet;
import repository.DummyData;
import repository.InMemoryTransactionRepository;
import repository.InMemoryWalletRepository;
import service.WalletServiceImpl;
import util.Format;
import java.util.List;
import java.util.Scanner;

// Main hanya mengatur tampilan dan membaca input.
// Aturan bisnis (validasi, saldo, riwayat) ada di WalletServiceImpl.
public class App {

    static Scanner scanner = new Scanner(System.in);
    static WalletService service;

    public static void main(String[] args) {
        // Siapkan data dummy lalu sambungkan ke service
        WalletRepository walletRepo = new InMemoryWalletRepository();
        TransactionRepository trxRepo = new InMemoryTransactionRepository();
        DummyData.seed(walletRepo, trxRepo);
        service = new WalletServiceImpl(walletRepo, trxRepo);

        while (true) {
            List<Wallet> wallets = service.getAllWallets();

            System.out.println();
            System.out.println("========== KODA E-WALLET ==========");
            System.out.println("Pilih Pengguna:");
            for (int i = 0; i < wallets.size(); i++) {
                System.out.println((i + 1) + ". " + wallets.get(i).getOwner().getFullName());
            }
            int exitNumber = wallets.size() + 1;
            System.out.println(exitNumber + ". Exit");

            int choice = readInt("Pilihan: ");
            if (choice == exitNumber) {
                System.out.println("Terima kasih, sampai jumpa!");
                return;
            } else if (choice >= 1 && choice <= wallets.size()) {
                walletMenu(wallets.get(choice - 1));
            } else {
                System.out.println("Pilihan tidak tersedia!");
            }
        }
    }

    // ---------------- MENU WALLET ----------------
    static void walletMenu(Wallet wallet) {
        while (true) {
            System.out.println();
            System.out.println("========== WALLET " + wallet.getOwner().getFullName().toUpperCase() + " ==========");
            System.out.println("1. Lihat Saldo");
            System.out.println("2. Top Up");
            System.out.println("3. Transfer");
            System.out.println("4. Riwayat Transaksi");
            System.out.println("5. Logout");
            int choice = readInt("Silahkan pilih menu: ");

            if (choice == 1) {
                showBalance(wallet);
            } else if (choice == 2) {
                topUp(wallet);
            } else if (choice == 3) {
                transfer(wallet);
            } else if (choice == 4) {
                showHistory(wallet);
            } else if (choice == 5) {
                return;
            } else {
                System.out.println("Pilihan tidak tersedia!");
            }
        }
    }

    static void showBalance(Wallet wallet) {
        System.out.println();
        System.out.println("Nomor Wallet : " + wallet.getWalletNumber());
        System.out.println("Nama Pemilik : " + wallet.getOwner().getFullName());
        System.out.println("Email        : " + wallet.getOwner().getEmail());
        System.out.println("No. HP       : " + wallet.getOwner().getPhone());
        System.out.println("Saldo        : " + Format.rupiah(wallet.getBalance()));
    }

    static void topUp(Wallet wallet) {
        System.out.println();
        System.out.println("========== TOP UP ==========");
        long amount = readLong("Nominal Top Up: ");

        try {
            service.validateAmount(amount); // tolak nominal salah sebelum menanyakan catatan
            String notes = readText("Catatan (opsional): ");
            if (notes.isEmpty()) {
                notes = "Top up saldo";
            }

            service.topUp(wallet, amount, notes);
            System.out.println("Top up berhasil!");
            System.out.println("Saldo sekarang : " + Format.rupiah(wallet.getBalance()));
        } catch (WalletException e) {
            System.out.println("Top up gagal: " + e.getMessage());
        }
    }

    static void transfer(Wallet sender) {
        System.out.println();
        System.out.println("========== TRANSFER ==========");
        String number = readText("Nomor Wallet Tujuan: ");

        try {
            Wallet receiver = service.findReceiver(sender, number);
            System.out.println("Penerima            : " + receiver.getOwner().getFullName());

            long amount = readLong("Nominal Transfer    : ");
            service.validateTransfer(sender, number, amount); // cek dulu sebelum konfirmasi

            String notes = readText("Catatan (opsional)  : ");
            if (notes.isEmpty()) {
                notes = "Transfer ke " + receiver.getOwner().getFullName();
            }

            if (!confirm("Konfirmasi Transfer (Y/n): ")) {
                System.out.println("Transfer dibatalkan.");
                return;
            }

            service.transfer(sender, number, amount, notes);
            System.out.println("Transfer berhasil!");
            System.out.println();
            System.out.println("Pengirim     : " + sender.getOwner().getFullName());
            System.out.println("Penerima     : " + receiver.getOwner().getFullName());
            System.out.println("Nominal      : " + Format.rupiah(amount));
            System.out.println("Sisa Saldo   : " + Format.rupiah(sender.getBalance()));
        } catch (WalletException e) {
            System.out.println("Transfer gagal: " + e.getMessage());
        }
    }

    static void showHistory(Wallet wallet) {
        String number = wallet.getWalletNumber();

        while (true) {
            System.out.println();
            System.out.println("Filter Riwayat:");
            System.out.println("1. Semua");
            System.out.println("2. Pemasukan");
            System.out.println("3. Pengeluaran");
            System.out.println("0. Kembali");
            int choice = readInt("Pilih: ");

            HistoryFilter filter;
            if (choice == 0) {
                return;
            } else if (choice == 1) {
                filter = HistoryFilter.ALL;
            } else if (choice == 2) {
                filter = HistoryFilter.INCOME;
            } else if (choice == 3) {
                filter = HistoryFilter.EXPENSE;
            } else {
                System.out.println("Pilihan tidak tersedia!");
                continue;
            }

            List<Transaction> list = service.getHistory(wallet, filter);

            System.out.println();
            System.out.println("========== RIWAYAT ==========");
            if (list.isEmpty()) {
                System.out.println("Belum ada transaksi.");
            }
            for (int i = 0; i < list.size(); i++) {
                Transaction t = list.get(i);
                String sign = t.isIncomeFor(number) ? "+" : "-";
                // getLabelFor() berbeda untuk tiap jenis transaksi (polymorphism)
                System.out.printf("%d. %-15s %s %-12s | %s | %s%n", i + 1, t.getLabelFor(number), sign,
                        Format.rupiah(t.getAmount()), Format.dateTime(t.getCreatedAt()), t.getNotes());
            }
            System.out.println();
            System.out.println("Total Pemasukan    : " + Format.rupiah(service.getTotalIncome(wallet)));
            System.out.println("Total Pengeluaran  : " + Format.rupiah(service.getTotalExpense(wallet)));
        }
    }

    // ---------------- INPUT ----------------
    static String readText(String question) {
        System.out.print(question);
        if (!scanner.hasNextLine()) {
            System.out.println();
            System.exit(0); // input habis, tutup program
        }
        return scanner.nextLine().trim();
    }

    // Terus bertanya sampai pengguna mengetik angka yang valid
    static int readInt(String question) {
        while (true) {
            try {
                return Integer.parseInt(readText(question));
            } catch (NumberFormatException e) {
                System.out.println("Input tidak valid! Masukkan angka.");
            }
        }
    }

    static long readLong(String question) {
        while (true) {
            try {
                return Long.parseLong(readText(question));
            } catch (NumberFormatException e) {
                System.out.println("Input tidak valid! Masukkan angka.");
            }
        }
    }

    // Y/n, kosong dianggap Y
    static boolean confirm(String question) {
        while (true) {
            String answer = readText(question).toLowerCase();
            if (answer.isEmpty() || answer.equals("y")) {
                return true;
            } else if (answer.equals("n")) {
                return false;
            }
            System.out.println("Input tidak valid! Ketik Y atau n.");
        }
    }
}