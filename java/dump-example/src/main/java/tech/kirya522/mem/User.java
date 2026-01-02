package tech.kirya522.mem;

public class User {
    private final long id;
    private final String name;
    private final long order;

    public User(long id, String name, long order) {
        this.id = id;
        this.name = name;
        this.order = order;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getOrder() {
        return order;
    }
}
