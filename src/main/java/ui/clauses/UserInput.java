package ui.clauses;

public class UserInput {
    private final Clause clause;
    private final UserCommand command;

    public UserInput(Clause clause, UserCommand command){
        this.clause = clause;
        this.command = command;
    }

    public Clause getClause() {
        return clause;
    }

    public UserCommand getCommand() {
        return command;
    }
}
