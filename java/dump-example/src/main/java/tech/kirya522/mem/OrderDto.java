package tech.kirya522.mem;

public class OrderDto {
    long id;
    String user;

    public OrderDto(Order o) {
        this.id = o.id;
        this.user = o.user;
    }
}