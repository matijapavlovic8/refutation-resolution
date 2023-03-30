package ui.clauses;

import java.util.Optional;

public class Resolvent {

    private Optional<Clause> resolvent;
    private Clause resolventClause;

    public Resolvent(Optional<Clause> resolvent) {
        this.resolvent = resolvent;
    }
}
