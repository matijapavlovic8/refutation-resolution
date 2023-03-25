package ui.clauses;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Clause {
    private List<Literal> literals;

    public Clause(List<Literal> literals) {
        this.literals = literals;
    }

    public List<Literal> getLiterals() {
        return literals;
    }

    public void setLiterals(List<Literal> literals) {
        this.literals = literals;
    }

    public void negate() {
        List<Literal> negatedLiterals = new ArrayList<>();
        for(Literal l: this.getLiterals()) {
            Literal neg = new Literal(l.getLiteral(), !l.isNegated());
            negatedLiterals.add(neg);
        }
        this.setLiterals(negatedLiterals);
    }

    public boolean isTautology() {
        List<Literal> literals = this.getLiterals();
        for(int i = 0; i < literals.size(); i++) {
            for (int j = i + 1; j < literals.size(); j++) {
                Literal l1 = literals.get(i);
                Literal l2 = literals.get(j);
                if(l1.checkOpposing(l2)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isRedundant(Clause other) {
        return new HashSet<>(other.getLiterals()).containsAll(getLiterals());
    }


}
