package ui.algorithms;

import ui.clauses.Clause;
import ui.clauses.ClausePair;
import ui.clauses.Clauses;
import ui.clauses.Literal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static ui.clauses.Clauses.eraseRedundantClauses;


public class RefutationResolution {

    public static Set<ClausePair> resolvedPairs = new HashSet<>();

    public static void resolution(Clauses premises) {

        Clause goalClause = premises.getGoalClause();
        Clause negatedGoalClause = goalClause.negate();

        Set<Clause> premisesCS = eraseRedundantClauses(premises.getClauses());
        premises.setClauses(premisesCS);

        Set<Clause> clauses = premises.getClauses();
        printKnowledge(clauses, negatedGoalClause);

        Set<Clause> sos = new HashSet<>();
        sos.add(negatedGoalClause);


        while (true) {
            Map<Clause, ClausePair> map = new HashMap<>();
            Set<Clause> resolvents = new HashSet<>();
            Set<ClausePair> pairs = selectClauses(clauses, sos);

            for(ClausePair pair: pairs) {
                Clause res = plResolve(pair);

                if (res.getLiterals().isEmpty()) {
                    printResolvents(map);
                    System.out.println("NIL " + "(" + pair.getC1() + ", "
                    + pair.getC2() + ")");
                    System.out.println("[CONCLUSION]: " + goalClause + " is true");
                    return;
                }
                if (!res.isTautology()) {
                    resolvents.add(res);
                    map.put(res, pair);
                }

            }
            resolvents.removeIf(Clause::isTautology);
            eraseRedundantClauses(resolvents);

            Set<Clause> union = new HashSet<>(sos);
            union.addAll(clauses);

            if (union.containsAll(resolvents)) {
                printResolvents(map);
                System.out.println("[CONCLUSION]: " + goalClause + " is unknown");
                return;
            }

            sos.addAll(resolvents);

            sos.removeIf(Clause::isTautology);
            printResolvents(map);
            System.out.println();

        }

    }

    private static Set<ClausePair> selectClauses (Set<Clause> clauses, Set<Clause> sos) {
        Set<Clause> union = new HashSet<>(clauses);
        union.addAll(sos);
        Set<ClausePair> pairs = new HashSet<>();
        for (Clause c1: union) {
            for (Clause c2: sos) {
                c1.getLiterals().forEach(l1 -> c2.getLiterals().forEach(l2 -> {
                    ClausePair cp = new ClausePair(c1, c2);
                    if (!c1.equals(c2) && l2.checkOpposing(l1) && !resolvedPairs.contains(cp)) {
                        pairs.add(cp);
                    }
                }));
            }
        }
        resolvedPairs.addAll(pairs);
        return pairs;
    }

    private static Clause plResolve (ClausePair pair) {
        Set<Literal> newClause = new HashSet<>();
        List<Literal> literals1 = pair.getC1().getLiterals();
        List<Literal> literals2 = pair.getC2().getLiterals();
        newClause.addAll(literals1);
        newClause.addAll(literals2);

        for (Literal l1: literals1) {
            for (Literal l2: literals2) {
                if (l1.checkOpposing(l2)) {
                    newClause.removeAll(List.of(l1, l2));
                }
            }
        }
        return new Clause(new ArrayList<>(newClause));
    }

    private static void printKnowledge(Set<Clause> clauses, Clause negatedGoalClause) {
        System.out.println("Knowledge:");
        System.out.println("================");
        for (Clause c: clauses) {
            System.out.println(c);
        }
        System.out.println(negatedGoalClause);
        System.out.println("================");
    }

    private static void printResolvents(Map<Clause, ClausePair> map) {
        for (Clause res: map.keySet()) {
            System.out.println(res + " (" + map.get(res).getC1() + ", "
                    + map.get(res).getC2() + ")");
        }
    }

    private static boolean isRedundant(Clause clause, Set<Clause> set) {
        for (Clause c: set) {
            if (new HashSet<>(c.getLiterals()).containsAll(clause.getLiterals())) {
                //System.out.println(clause + " ******" +set);
                return true;
            }
        }
        return false;
    }

}
