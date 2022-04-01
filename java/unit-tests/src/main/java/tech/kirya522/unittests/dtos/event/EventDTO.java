package tech.kirya522.unittests.dtos.event;

/**
 * All dates are used unix timestamp https://www.unixtimestamp.com/
 */
public class EventDTO {
    private long id;
    private String name;
    private long startDate;
    private long endDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }
}

