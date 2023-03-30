package ui.algorithms;

import ui.clauses.Clause;
import ui.clauses.ClausePair;
import ui.clauses.Clauses;
import ui.clauses.Literal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RefutationResolution {

    public static Clauses premises;
    public static Clause goalClause;
    public static Clause negatedGoalClause;
    public static boolean result;
    public static List<Clause> resultClauses;

    public static void resolution(Clauses premises) {
        resultClauses = new ArrayList<>();
        RefutationResolution.premises = premises;
        goalClause = premises.extractGoal();
        negatedGoalClause = goalClause.negate();
        premises.eraseRedundantClauses();
        resultClauses.addAll(premises.getClauses());
        for (int i = 0; i < premises.getClauses().size(); i++) {
            System.out.println(i + 1 + ". " + premises.getClauses().get(i));
        }
        System.out.println(premises.getClauses().size() + 1 + ". " + negatedGoalClause);
        System.out.println("=================");
        List<Clause> sosClauseList = new ArrayList<>();
        sosClauseList.add(negatedGoalClause);
        Clauses sos = new Clauses(sosClauseList);
        Clauses allClauses = new Clauses(resultClauses);
        while (true) {
            sos.removeTautologies();
            allClauses.removeTautologies();
            Set<ClausePair> pairs = selectClauses(allClauses, sos);

            if(pairs.isEmpty()) {
                System.out.println("[CONCLUSION]: " + RefutationResolution.goalClause + " is false");
                return;
            }

            for (ClausePair pair: pairs) {
                Clauses resolvents = plResolve(pair);

                if (resolvents.getClauses().isEmpty()) {
                    int index = resultClauses.size() + 1;
                    System.out.println(index + ". NIL ("
                        + getIndexOfClause(pair.getC1())
                        + ", "
                        + getIndexOfClause(pair.getC2())
                        + ")"
                    );
                    System.out.println("[CONCLUSION]: " + RefutationResolution.goalClause + " is true");
                    return;
                }

                for (Clause c: resolvents.getClauses()) {
                    System.out.println(getIndexOfClause(c)
                        + ". "
                        + c
                        + "("
                        + getIndexOfClause(pair.getC1())
                        + ", "
                        + getIndexOfClause(pair.getC2())
                        + ")"
                    );
                }
                resolvents.removeTautologies();
                if (resolvents.getClauses().isEmpty()) {
                    System.out.println("[CONCLUSION]: " + RefutationResolution.goalClause + " is false");
                    return;
                }
                resolvents.eraseRedundantClauses();
                sos.eraseRedundantClauses();

                List<Clause> clauseList = allClauses.getClauses();
                clauseList.addAll(resolvents.getClauses());
                allClauses.setClauses(clauseList);

                List<Clause> sosList = sos.getClauses();
                sosList.addAll(resolvents.getClauses());
                sos.setClauses(sosList);
            }
        }
    }

    private static Set<ClausePair> selectClauses (Clauses clauses, Clauses sos) {
        Set<ClausePair> pairs = new HashSet<>();

        for (Clause c1: clauses.getClauses()) {
            for (Clause c2: sos.getClauses()) {
                c1.getLiterals().forEach(l1 -> {
                    for (Literal l2: c2.getLiterals()) {
                        if (l1.checkOpposing(l2)) {
                            pairs.add(new ClausePair(c1, c2));
                            break;
                        }
                    }
                });
            }
        }
        return pairs;
    }

    private static Clauses plResolve (ClausePair pair) {

        List<Clause> resolvents = new ArrayList<>();
        List<Literal> removed = new ArrayList<>();

        for (Literal l1: pair.getC1().getLiterals()) {
            for (Literal l2: pair.getC2().getLiterals()) {
                if(l1.checkOpposing(l2)) {
                    removed.add(l1);
                }
            }
        }

        for (Literal l1: removed) {
            List<Literal> literals = new ArrayList<>(pair.getC1().getLiterals());
            literals.addAll(pair.getC2().getLiterals());
            literals.removeIf(l2 -> l2.checkOpposing(l1));
            if (!literals.isEmpty()) {
                resolvents.add(new Clause(literals));
            }
        }
        return new Clauses(resolvents);
    }

    private static int getIndexOfClause(Clause c) {
        int index = 0;
        for (Clause c1: resultClauses) {
            index++;
            if(c1.equals(c)) {
                return index;
            }
        }
        return -1;
    }
}
