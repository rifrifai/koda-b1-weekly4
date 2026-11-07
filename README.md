# Koda E-Wallet (Weekly 4)

Aplikasi e-wallet pakai console, dibuat dua versi: Java dan C# (.NET).
Fitur dan aturannya sama di kedua versi. Isi folder:

- `Java` : versi Java
- `Dotnet` : versi C# (.NET 10)

## Fitur

- Pilih pengguna (ada 3 data dummy: Andi, Budi, Citra)
- Lihat saldo
- Top up (nominal harus lebih dari 0)
- Transfer ke wallet lain pakai nomor wallet (contoh: WAL002)
- Riwayat transaksi, bisa difilter semua / pemasukan / pengeluaran

## Alur Aplikasi

1. Pilih pengguna (Andi / Budi / Citra) atau Exit
2. Masuk ke menu wallet: Lihat Saldo, Top Up, Transfer, Riwayat Transaksi, Logout
3. Top Up: isi nominal, saldo bertambah, transaksi masuk riwayat
4. Transfer: isi nomor wallet tujuan, nama penerima muncul, isi nominal, konfirmasi Y/n, saldo kedua wallet berubah
5. Riwayat: pilih filter, muncul daftar transaksi dan total pemasukan/pengeluaran
6. Logout untuk kembali ke pilih pengguna

## Struktur Class

Java (folder `Java/weekly-app/src`) dan .NET (folder `Dotnet`) isinya sama, hanya beda penulisan.

- `model` / `Models` : `User`, `Wallet`, `Transaction`, `TopUpTransaction`, `TransferTransaction`, `HistoryFilter`
- `interfaces` / `Interfaces` : interface untuk repository dan service
- `repository` / `Repositories` : penyimpanan data di List + data dummy
- `service` / `Services` : semua logika (validasi, top up, transfer, riwayat)
- `exception` / `Exceptions` : error buatan sendiri (nominal salah, saldo kurang, wallet tidak ketemu, transfer ke diri sendiri)
- `App.java` / `Program.cs` : cuma untuk tampilan dan input

Catatan: di Java nama foldernya `interfaces` karena `interface` tidak boleh dipakai sebagai nama package.

## Penerapan OOP

- Class, Object, Constructor : semua class di atas
- Encapsulation : field dibuat private, saldo hanya berubah lewat `deposit` dan `withdraw` (di .NET `Deposit` dan `Withdraw`)
- Inheritance : `TopUpTransaction` dan `TransferTransaction` turunan dari `Transaction`
- Abstraction : `Transaction` adalah abstract class
- Polymorphism : `getLabelFor()` hasilnya beda tiap jenis transaksi (Top Up, Transfer Masuk, Transfer Keluar)
- Interface : repository dan service dipakai lewat interface
- Collections : `List<Wallet>` dan `List<Transaction>`
- Lambda & Stream (Java) / LINQ (C#) : cari wallet, filter riwayat, urutkan, hitung total
- Exception handling : semua error ditangkap dengan try-catch, input bukan angka diminta ulang

## Aturan Penting

- Saldo tidak boleh minus
- Transfer ditolak kalau penerima tidak ketemu, ke diri sendiri, nominal 0 atau minus, atau saldo kurang
- Kalau transfer gagal, saldo dan riwayat tidak berubah (semua dicek dulu sebelum data diubah)

## Data Dummy

| Wallet | Nama | Saldo |
|---|---|---|
| WAL001 | Andi | Rp500.000 |
| WAL002 | Budi | Rp350.000 |
| WAL003 | Citra | Rp350.000 |

## Cara Menjalankan

Java (butuh JDK 17 atau lebih baru), jalankan dari folder `Java`:

```
mvn compile
mvn test
mvn install
java -cp target/classes com.koda.App
```

.NET (butuh .NET SDK 10), jalankan dari folder `Dotnet/KodaEWallet`:

```
dotnet run
```

## Screenshot Pengujian

Screenshot ada di folder `Java/weekly-app/src/screenshots` dan `Dotnet/KodaEWallet/screenshots`.

Yang diuji:

1. Lihat saldo
2. Top up
3. Top up dengan nominal 0 / minus / huruf (ditolak)
4. Transfer berhasil
5. Transfer ke wallet yang tidak ada
6. Transfer ke diri sendiri
7. Transfer dengan nominal 0 / minus
8. Transfer lebih besar dari saldo
9. Riwayat dan filter pemasukan / pengeluaran

Contoh hasil transfer Andi ke Budi:

```
========== TRANSFER ==========
Nomor Wallet Tujuan: WAL002
Penerima            : Budi
Nominal Transfer    : 100000
Catatan (opsional)  : Bayar makan
Konfirmasi Transfer (Y/n): y
Transfer berhasil!

Pengirim     : Andi
Penerima     : Budi
Nominal      : Rp100.000
Sisa Saldo   : Rp400.000
```
