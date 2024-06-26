package ui.algorithms;

import ui.clauses.Clause;
import ui.clauses.ClausePair;
import ui.clauses.Clauses;
import ui.clauses.Literal;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;


import static ui.clauses.Clauses.eraseRedundantClauses;


public class RefutationResolution {

    public static Set<ClausePair> resolvedPairs;
    public static Set<Clause> usedClauses;
    public static List<Clause> negatedGoalsList = new ArrayList<>();


    public static void resolution(Clauses premises) {
        resolvedPairs = new HashSet<>();
        usedClauses = new HashSet<>();
        if (negatedGoalsList.isEmpty()) {
            getNegatedGoalClause(premises);
        }

        Clause goalClause = premises.getGoalClause();

        Clause negatedGoalClause = negatedGoalsList.get(0);
        negatedGoalsList.remove(negatedGoalClause);

        Set<Clause> clauses = premises.getClauses();
        printKnowledge(clauses, negatedGoalClause);

        Set<Clause> sos = new HashSet<>();
        sos.add(negatedGoalClause);

        Map<Clause, ClausePair> map = new LinkedHashMap<>();
        while (true) {

            Set<Clause> resolvents = new HashSet<>();
            Set<ClausePair> pairs = selectClauses(clauses, sos);

            for(ClausePair pair: pairs) {
                Clause res = plResolve(pair);

                if (res.getLiterals().isEmpty()) {
                    map.put(res, pair);
                    printResolvents(map);
                    System.out.println("NIL " + "(" + pair.getC1() + ", "
                    + pair.getC2() + ")");
                    System.out.println("[CONCLUSION]: " + goalClause + " is true");
                    return;
                }
                resolvents.add(res);
                map.put(res, pair);


            }
            resolvents.removeIf(Clause::isTautology);
            eraseRedundantClauses(resolvents);

            Set<Clause> union = new HashSet<>(sos);
            union.addAll(clauses);

            if (union.containsAll(resolvents)) {
                if(!negatedGoalsList.isEmpty()){
                    resolution(premises);
                }
                printResolvents(map);
                System.out.println("[CONCLUSION]: " + goalClause + " is unknown");
                return;
            }
            sos.addAll(resolvents);
            sos.removeIf(Clause::isTautology);

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
                    return new Clause(new ArrayList<>(newClause));
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
        System.out.println("Negated goal clause:");
        System.out.println("================");
        System.out.println(negatedGoalClause);
        System.out.println("================");
    }

    private static void printResolvents(Map<Clause, ClausePair> map) {
        fillUsed(map);
        for (Clause res: map.keySet()) {
            if(usedClauses.contains(res) && !res.getLiterals().isEmpty()) {
                System.out.println(res + " (" + map.get(res).getC1() + ", "
                        + map.get(res).getC2() + ")");
            }
        }
    }

    private static void fillUsed(Map<Clause, ClausePair> map) {
        Queue<Clause> pom = new ArrayDeque<>();
        if (map.isEmpty())
            return;
        Clause last = map.keySet().iterator().next();
        for (Clause c: map.keySet()) {
            last = c;
        }
        pom.add(last);

        while (!pom.isEmpty()) {
            Clause curr = pom.poll();
            if (curr != null) {
                if (map.containsKey(curr) && !usedClauses.contains(map.get(curr).getC1()) && !usedClauses.contains(map.get(curr).getC2())) {
                    pom.add(map.get(curr).getC1());
                    pom.add(map.get(curr).getC2());
                    usedClauses.addAll(pom);
                }
            }
        }
    }

    private static void getNegatedGoalClause(Clauses premises) {
        Clause goalClause = premises.getGoalClause();
        if (goalClause.getLiterals().size() == 1) {
            negatedGoalsList.add(goalClause.negate());
            return;
        }

        for (Literal l: goalClause.getLiterals()) {
            Clause newClause = new Clause(List.of(l));
            negatedGoalsList.add(newClause.negate());
        }
    }

}
