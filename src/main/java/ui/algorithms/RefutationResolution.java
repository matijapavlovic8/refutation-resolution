package ui.algorithms;

import ui.clauses.Clause;
import ui.clauses.Clauses;

import java.util.HashSet;
import java.util.Set;

public class RefutationResolution {

    public static void resolution(Clauses clauses) {
        Clause goalClause = clauses.extractGoal();
        Clause negatedGoalClause = goalClause;
        negatedGoalClause.negate();

        Set<Clause> sos = new HashSet<>();
        sos.add(negatedGoalClause);



    }
}
