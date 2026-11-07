namespace KodaEWallet.Models;

public abstract class Transaction
{
    public int Id { get; }
    public string? SenderNumber { get; }
    public string ReceiverNumber { get; }
    public long Amount { get; }
    public string Notes { get; }
    public DateTime CreatedAt { get; }
    protected Transaction(int id, string? senderNumber, string receiverNumber, long amount, string notes, DateTime createdAt)
    {
        Id = id;
        SenderNumber = senderNumber;
        ReceiverNumber = receiverNumber;
        Amount = amount;
        Notes = notes;
        CreatedAt = createdAt;
    }

    public bool IsIncomeFor(string walletNumber) => ReceiverNumber == walletNumber;
    public abstract string TypeName { get; }
    public abstract string GetLabelFor(string walletNumber);
}
