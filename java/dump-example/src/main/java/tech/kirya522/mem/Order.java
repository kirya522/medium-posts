package tech.kirya522.mem;

public class Order {
    long id;
    String user;
    byte[] payload;

    public Order(long id) {
        this.id = id;
        this.user = "user-" + id;
        this.payload = new byte[512 * 1024]; // 512 KB
    }
}