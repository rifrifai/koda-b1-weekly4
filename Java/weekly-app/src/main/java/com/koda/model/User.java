package model;

// Data pemilik akun (sesuai tabel users di database Weekly 3)
public class User {
    private int id;
    private String username;
    private String fullName;
    private String email;
    private String phone;

    // CONSTRUCTOR
    public User(int id, String username, String fullName, String email, String phone) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
    }

    // ENCAPSULATION: field private, dibaca lewat getter
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
}
