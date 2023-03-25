package ui.clauses;

import java.util.Objects;

public class Literal {
    private final String literal;
    private final boolean negated;


    public Literal(String literal, boolean negated){
        this.literal = literal;
        this.negated = negated;
    }

    @Override
    public String toString() {
        return negated ? "~" : "" + literal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Literal literal1)) {
            return false;
        }
        return negated == literal1.negated && literal.equals(literal1.literal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(literal, negated);
    }

    public String getLiteral() {
        return literal;
    }

    public boolean isNegated() {
        return negated;
    }

    public boolean checkOpposing(Literal other) {
        return this.getLiteral().equals(other.getLiteral())
            && this.isNegated() != other.isNegated();
    }
}
