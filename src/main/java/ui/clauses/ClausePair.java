package ui.clauses;

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
}
