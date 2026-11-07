package model;

import java.time.LocalDateTime;

// INHERITANCE: transfer antar dua wallet
public class TransferTransaction extends Transaction {

    public TransferTransaction(int id, String senderNumber, String receiverNumber,
                               long amount, String notes, LocalDateTime createdAt) {
        super(id, senderNumber, receiverNumber, amount, notes, createdAt);
    }

    @Override
    public String getType() {
        return "transfer";
    }

    // Label berbeda tergantung wallet yang sedang melihat riwayat
    @Override
    public String getLabelFor(String walletNumber) {
        if (isIncomeFor(walletNumber)) {
            return "Transfer Masuk";
        }
        return "Transfer Keluar";
    }
}
