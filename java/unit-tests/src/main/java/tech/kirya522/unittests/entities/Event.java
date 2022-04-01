package tech.kirya522.unittests.entities;

public class Event {
    private final long id;
    private final String name;
    private final long startDate;
    private final long endDate;

    public Event(long id, String name, long startDate, long endDate) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getStartDate() {
        return startDate;
    }

    public long getEndDate() {
        return endDate;
    }
}
