namespace KodaEWallet.Models;

public class TopUpTransaction : Transaction
{
    public TopUpTransaction(int id, string receiverNumber, long amount, string notes, DateTime createdAt)
        : base(id, null, receiverNumber, amount, notes, createdAt)
    {
    }

    public override string TypeName => "topup";

    public override string GetLabelFor(string walletNumber) => "Top Up";
}
