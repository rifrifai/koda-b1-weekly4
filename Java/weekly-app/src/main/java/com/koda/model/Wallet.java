package model;

import exception.InsufficientBalanceException;
import exception.InvalidAmountException;

// Dompet milik satu User (sesuai tabel wallets)
public class Wallet {
    private String walletNumber;
    private User owner;
    private long balance; // private: tidak bisa diubah sembarangan dari luar

    public Wallet(String walletNumber, User owner) {
        this.walletNumber = walletNumber;
        this.owner = owner;
        this.balance = 0;
    }

    public String getWalletNumber() { return walletNumber; }
    public User getOwner() { return owner; }
    public long getBalance() { return balance; }

    // Saldo HANYA bisa berubah lewat deposit() dan withdraw(), keduanya ada validasi.
    public void deposit(long amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException();
        }
        balance = balance + amount;
    }

    public void withdraw(long amount) throws InvalidAmountException, InsufficientBalanceException {
        if (amount <= 0) {
            throw new InvalidAmountException();
        }
        if (amount > balance) {
            throw new InsufficientBalanceException(balance, amount); // saldo tidak boleh minus
        }
        balance = balance - amount;
    }
}
