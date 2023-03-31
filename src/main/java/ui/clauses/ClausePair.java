package ui.clauses;

import java.util.Objects;

public class ClausePair {
    private Clause c1;
    private Clause c2;
    public ClausePair(Clause c1, Clause c2) {
        this.c1 = c1;
        this.c2 = c2;
    }

    public Clause getC1() {
        return c1;
    }

    public Clause getC2() {
        return c2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClausePair that = (ClausePair) o;
        return (c1.equals(that.c1) && c2.equals(that.c2)) ||
                (c1.equals(that.c2) && c2.equals(that.c1));
    }

    @Override
    public int hashCode() {
        return Objects.hash(c1, c2);
    }
}
