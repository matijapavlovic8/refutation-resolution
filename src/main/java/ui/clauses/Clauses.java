package ui.clauses;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Clauses {
    private Set<Clause> clauses;
    private Clause goalClause;
    public Clauses(Set<Clause> clauses, Clause goalClause) {
        this.clauses = clauses;
        this.goalClause = goalClause;
    }

    public Set<Clause> getClauses() {
        return clauses;
    }

    public void setClauses(Set<Clause> clauses) {
        this.clauses = clauses;
    }

    public Set<Clause> removeTautologies(Set<Clause> clauseSet) {
        clauseSet.removeIf(Clause::isTautology);
        return clauseSet;
    }

    public Clause getGoalClause() {
        return goalClause;
    }

    public void setGoalClause(Clause newGoal) {
        this.goalClause = newGoal;
    }

    public static Set<Clause> eraseRedundantClauses(Set<Clause> clauseSet) {
        Set<Clause> removed = new HashSet<>();
        for (Clause c1: clauseSet) {
            for (Clause c2: clauseSet) {
                if (!c1.equals(c2)) {
                    if (c1.isRedundant(c2)) {
                        removed.add(c2);
                    } else if (c2.isRedundant(c1)) {
                        removed.add(c1);
                    }
                }
            }
        }
        clauseSet.removeAll(removed);
        return clauseSet;
    }
}
