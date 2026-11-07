package model;

import java.time.LocalDateTime;

// INHERITANCE: TopUpTransaction adalah sebuah Transaction (tanpa pengirim)
public class TopUpTransaction extends Transaction {

    public TopUpTransaction(int id, String receiverNumber, long amount,
                            String notes, LocalDateTime createdAt) {
        super(id, null, receiverNumber, amount, notes, createdAt);
    }

    @Override
    public String getType() {
        return "topup";
    }

    @Override
    public String getLabelFor(String walletNumber) {
        return "Top Up";
    }
}
