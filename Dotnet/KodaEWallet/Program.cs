using KodaEWallet.Exceptions;
using KodaEWallet.Interfaces;
using KodaEWallet.Models;
using KodaEWallet.Repositories;
using KodaEWallet.Services;
using KodaEWallet.Utils;

namespace KodaEWallet;

public class Program
{
    private static IWalletService service = null!;

    public static void Main(string[] args)
    {
        IWalletRepository walletRepo = new InMemoryWalletRepository();
        ITransactionRepository trxRepo = new InMemoryTransactionRepository();
        DummyData.Seed(walletRepo, trxRepo);
        service = new WalletService(walletRepo, trxRepo);

        while (true)
        {
            List<Wallet> wallets = service.GetAllWallets();

            Console.WriteLine();
            Console.WriteLine("========== KODA E-WALLET ==========");
            Console.WriteLine("Pilih Pengguna:");
            for (int i = 0; i < wallets.Count; i++)
            {
                Console.WriteLine($"{i + 1}. {wallets[i].Owner.FullName}");
            }
            int exitNumber = wallets.Count + 1;
            Console.WriteLine($"{exitNumber}. Exit");

            int choice = ReadInt("Pilihan: ");
            if (choice == exitNumber)
            {
                Console.WriteLine("Terima kasih, sampai jumpa!");
                return;
            }
            else if (choice >= 1 && choice <= wallets.Count)
            {
                WalletMenu(wallets[choice - 1]);
            }
            else
            {
                Console.WriteLine("Pilihan tidak tersedia!");
            }
        }
    }

    // ---------------- MENU WALLET ----------------
    static void WalletMenu(Wallet wallet)
    {
        while (true)
        {
            Console.WriteLine();
            Console.WriteLine($"========== WALLET {wallet.Owner.FullName.ToUpper()} ==========");
            Console.WriteLine("1. Lihat Saldo");
            Console.WriteLine("2. Top Up");
            Console.WriteLine("3. Transfer");
            Console.WriteLine("4. Riwayat Transaksi");
            Console.WriteLine("5. Logout");
            int choice = ReadInt("Silahkan pilih menu: ");

            if (choice == 1)
                ShowBalance(wallet);
            else if (choice == 2)
                TopUp(wallet);
            else if (choice == 3)
                Transfer(wallet);
            else if (choice == 4)
                ShowHistory(wallet);
            else if (choice == 5)
                return;
            else
                Console.WriteLine("Pilihan tidak tersedia!");
        }
    }

    static void ShowBalance(Wallet wallet)
    {
        Console.WriteLine();
        Console.WriteLine("Nomor Wallet : " + wallet.WalletNumber);
        Console.WriteLine("Nama Pemilik : " + wallet.Owner.FullName);
        Console.WriteLine("Email        : " + wallet.Owner.Email);
        Console.WriteLine("No. HP       : " + wallet.Owner.Phone);
        Console.WriteLine("Saldo        : " + Format.Rupiah(wallet.Balance));
    }

    static void TopUp(Wallet wallet)
    {
        Console.WriteLine();
        Console.WriteLine("========== TOP UP ==========");
        long amount = ReadLong("Nominal Top Up: ");

        try
        {
            service.ValidateAmount(amount);
            string notes = ReadText("Catatan (opsional): ");
            if (notes == "")
                notes = "Top up saldo";

            service.TopUp(wallet, amount, notes);
            Console.WriteLine("Top up berhasil!");
            Console.WriteLine("Saldo sekarang : " + Format.Rupiah(wallet.Balance));
        }
        catch (WalletException e)
        {
            Console.WriteLine("Top up gagal: " + e.Message);
        }
    }

    static void Transfer(Wallet sender)
    {
        Console.WriteLine();
        Console.WriteLine("========== TRANSFER ==========");
        string number = ReadText("Nomor Wallet Tujuan: ");

        try
        {
            Wallet receiver = service.FindReceiver(sender, number);
            Console.WriteLine("Penerima            : " + receiver.Owner.FullName);

            long amount = ReadLong("Nominal Transfer    : ");
            service.ValidateTransfer(sender, number, amount); // cek dulu sebelum konfirmasi

            string notes = ReadText("Catatan (opsional)  : ");
            if (notes == "")
                notes = "Transfer ke " + receiver.Owner.FullName;

            if (!Confirm("Konfirmasi Transfer (Y/n): "))
            {
                Console.WriteLine("Transfer dibatalkan.");
                return;
            }

            service.Transfer(sender, number, amount, notes);
            Console.WriteLine("Transfer berhasil!");
            Console.WriteLine();
            Console.WriteLine("Pengirim     : " + sender.Owner.FullName);
            Console.WriteLine("Penerima     : " + receiver.Owner.FullName);
            Console.WriteLine("Nominal      : " + Format.Rupiah(amount));
            Console.WriteLine("Sisa Saldo   : " + Format.Rupiah(sender.Balance));
        }
        catch (WalletException e)
        {
            Console.WriteLine("Transfer gagal: " + e.Message);
        }
    }

    static void ShowHistory(Wallet wallet)
    {
        string number = wallet.WalletNumber;

        while (true)
        {
            Console.WriteLine();
            Console.WriteLine("Filter Riwayat:");
            Console.WriteLine("1. Semua");
            Console.WriteLine("2. Pemasukan");
            Console.WriteLine("3. Pengeluaran");
            Console.WriteLine("0. Kembali");
            int choice = ReadInt("Pilih: ");

            HistoryFilter filter;
            if (choice == 0)
            {
                return;
            }
            else if (choice == 1)
            {
                filter = HistoryFilter.All;
            }
            else if (choice == 2)
            {
                filter = HistoryFilter.Income;
            }
            else if (choice == 3)
            {
                filter = HistoryFilter.Expense;
            }
            else
            {
                Console.WriteLine("Pilihan tidak tersedia!");
                continue;
            }

            List<Transaction> list = service.GetHistory(wallet, filter);

            Console.WriteLine();
            Console.WriteLine("========== RIWAYAT ==========");
            if (list.Count == 0)
                Console.WriteLine("Belum ada transaksi.");

            for (int i = 0; i < list.Count; i++)
            {
                Transaction t = list[i];
                string sign = t.IsIncomeFor(number) ? "+" : "-";
                Console.WriteLine($"{i + 1}. {t.GetLabelFor(number),-15} {sign} {Format.Rupiah(t.Amount),-12} | {Format.DateTimeText(t.CreatedAt)} | {t.Notes}");
            }
            Console.WriteLine();
            Console.WriteLine("Total Pemasukan    : " + Format.Rupiah(service.GetTotalIncome(wallet)));
            Console.WriteLine("Total Pengeluaran  : " + Format.Rupiah(service.GetTotalExpense(wallet)));
        }
    }

    // ---------------- INPUT ----------------
    static string ReadText(string question)
    {
        Console.Write(question);
        string? line = Console.ReadLine();
        if (line == null)
        {
            Console.WriteLine();
            Environment.Exit(0);
        }
        return line!.Trim();
    }

    static int ReadInt(string question)
    {
        while (true)
        {
            if (int.TryParse(ReadText(question), out int value))
                return value;
            Console.WriteLine("Input tidak valid! Masukkan angka.");
        }
    }

    static long ReadLong(string question)
    {
        while (true)
        {
            if (long.TryParse(ReadText(question), out long value))
                return value;
            Console.WriteLine("Input tidak valid! Masukkan angka.");
        }
    }

    static bool Confirm(string question)
    {
        while (true)
        {
            string answer = ReadText(question).ToLower();
            if (answer == "" || answer == "y")
                return true;
            if (answer == "n")
                return false;
            Console.WriteLine("Input tidak valid! Ketik Y atau n.");
        }
    }
}
