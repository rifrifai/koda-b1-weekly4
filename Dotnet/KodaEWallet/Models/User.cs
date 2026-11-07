namespace KodaEWallet.Models;

public class User
{
    public int Id { get; }
    public string Username { get; }
    public string FullName { get; }
    public string Email { get; }
    public string Phone { get; }

    public User(int id, string username, string fullName, string email, string phone)
    {
        Id = id;
        Username = username;
        FullName = fullName;
        Email = email;
        Phone = phone;
    }
}
