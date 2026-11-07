namespace KodaEWallet.Models;

public class TransferTransaction : Transaction
{
    public TransferTransaction(int id, string senderNumber, string receiverNumber, long amount, string notes, DateTime createdAt)
        : base(id, senderNumber, receiverNumber, amount, notes, createdAt)
    {
    }

    public override string TypeName => "transfer";
    public override string GetLabelFor(string walletNumber)
    {
        if (IsIncomeFor(walletNumber))
            return "Transfer Masuk";

        return "Transfer Keluar";
    }
}
