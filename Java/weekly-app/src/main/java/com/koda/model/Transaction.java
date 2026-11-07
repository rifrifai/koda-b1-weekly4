package model;

import java.time.LocalDateTime;

// ABSTRACTION: kerangka umum semua transaksi (sesuai tabel transactions).
// Tidak bisa dibuat langsung, harus lewat TopUpTransaction atau TransferTransaction.
public abstract class Transaction {
    private int id;
    private String senderNumber;    // null kalau top up
    private String receiverNumber;
    private long amount;
    private String notes;
    private LocalDateTime createdAt;

    public Transaction(int id, String senderNumber, String receiverNumber,
                       long amount, String notes, LocalDateTime createdAt) {
        this.id = id;
        this.senderNumber = senderNumber;
        this.receiverNumber = receiverNumber;
        this.amount = amount;
        this.notes = notes;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public String getSenderNumber() { return senderNumber; }
    public String getReceiverNumber() { return receiverNumber; }
    public long getAmount() { return amount; }
    public String getNotes() { return notes; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Pemasukan = wallet tersebut sebagai penerima. Selain itu pengeluaran.
    public boolean isIncomeFor(String walletNumber) {
        return receiverNumber.equals(walletNumber);
    }

    // POLYMORPHISM: tiap jenis transaksi punya cara sendiri menamai dirinya
    public abstract String getType();

    public abstract String getLabelFor(String walletNumber);
}
