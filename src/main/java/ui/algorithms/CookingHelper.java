package ui.algorithms;

import ui.clauses.Clause;
import ui.clauses.Clauses;
import ui.clauses.UserCommand;
import ui.clauses.UserInput;
import ui.clauses.UserInputs;

import java.util.HashSet;
import java.util.Set;

public class CookingHelper {
    public static void cook(Clauses clauses, UserInputs userInputs){
        Set<Clause> clauseSet = clauses.getClauses();
        clauseSet.add(clauses.getGoalClause());
        clauses.setClauses(clauseSet);

        for (UserInput input: userInputs.getInputs()) {
            if(input.getCommand().equals(UserCommand.QUERY)) {
                Clause goalClause = input.getClause();
                clauses.setGoalClause(goalClause);
                RefutationResolution.resolvedPairs = new HashSet<>();;
                RefutationResolution.usedClauses = new HashSet<>();;
                RefutationResolution.resolution(clauses);
            } else if (input.getCommand().equals(UserCommand.ADD)) {
                Set<Clause> newClauses = clauses.getClauses();
                newClauses.add(input.getClause());
                clauses.setClauses(newClauses);
            } else if (input.getCommand().equals(UserCommand.REMOVE)) {
                clauses.getClauses().remove(input.getClause());
            }
        }
    }
}
