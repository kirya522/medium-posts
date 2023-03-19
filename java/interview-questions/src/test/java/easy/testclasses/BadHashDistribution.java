package easy.testclasses;

import java.util.Objects;

public class BadHashDistribution {
    private final static String BAD_HASH = "BAD_HASH";

    private final String name;

    public BadHashDistribution(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BadHashDistribution that = (BadHashDistribution) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(BAD_HASH);
    }
}
