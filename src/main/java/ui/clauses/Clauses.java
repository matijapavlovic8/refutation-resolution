package ui.clauses;

import java.util.HashSet;
import java.util.List;

public class Clauses {
    private List<Clause> clauses;
    public Clauses(List<Clause> clauses) {
        this.clauses = clauses;
    }

    public List<Clause> getClauses() {
        return clauses;
    }

    public void setClauses(List<Clause> clauses) {
        this.clauses = clauses;
    }

    public Clause extractGoal() {
        List<Clause> list = this.getClauses();
        Clause goalClause = list.remove(list.size() - 1);
        this.setClauses(list);
        return goalClause;
    }

    public void eraseRedundantClauses() {
        List<Clause> clauseList = this.getClauses();
        for (int i = 0; i < clauseList.size(); i++) {
            for (int j = i + 1; j < clauseList.size(); j++) {
                if (clauseList.get(i).isRedundant(clauseList.get(j))) {
                    clauseList.remove(clauseList.get(i));
                } else if(clauseList.get(j).isRedundant(clauseList.get(i))) {
                    clauseList.remove(clauseList.get(j));
                }
            }
        }
        this.setClauses(clauseList);
    }

    public void removeTautologies() {
        List<Clause> clauseList = this.getClauses();
        clauseList.removeIf(Clause::isTautology);
        this.setClauses(clauseList);
    }

}
