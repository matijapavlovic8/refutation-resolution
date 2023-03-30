package ui.clauses;

import java.util.Objects;

public class Literal {
    private final String literal;
    private boolean negated;


    public Literal(String literal, boolean negated){
        this.literal = literal;
        this.negated = negated;
    }

    @Override
    public String toString() {
        if (isNegated()) {
            return "~" + this.literal;
        }
        return this.literal;
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

    public void negate() {
        this.negated = !this.negated;
    }
}
