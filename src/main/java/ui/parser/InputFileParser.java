package ui.parser;

import ui.clauses.Clause;
import ui.clauses.Clauses;
import ui.clauses.Literal;
import ui.clauses.UserCommand;
import ui.clauses.UserInput;
import ui.clauses.UserInputs;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InputFileParser {

    private static final String COMMENT = "#";
    public static Clauses parseClauseFile(String path) {
        List<Clause> clauses = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line = br.readLine();
            while (line != null) {
                if (line.startsWith(COMMENT)) {
                    continue;
                }
                Clause clause = parseClause(line);
                if(!clause.isTautology()) {
                    clauses.add(clause);
                }
                line = br.readLine();
            }
            return new Clauses(clauses);
        } catch (IOException exc) {
            throw new RuntimeException(exc.getMessage());
        }
    }

    public static UserInputs parseUserInputs(String path) {
        List<UserInput> inputs = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line = br.readLine();
            while (line != null) {
                if (line.startsWith(COMMENT)) {
                    continue;
                }
                String command = line.substring(line.length() - 2, line.length() - 1);
                Clause c = parseClause(line.substring(0, line.length() - 2).trim());
                String QUERY = "?";
                String REMOVAL = "-";
                String ADDITION = "+";
                if (command.equals(QUERY)) {
                    inputs.add(new UserInput(c, UserCommand.QUERY));
                } else if (command.equals(ADDITION)) {
                    inputs.add(new UserInput(c, UserCommand.ADD));
                } else if(command.equals(REMOVAL)) {
                    inputs.add(new UserInput(c, UserCommand.REMOVE));
                } else {
                    throw new RuntimeException("Illegal command given!");
                }
                line = br.readLine();
            }
            return new UserInputs(inputs);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Clause parseClause (String line) {
        List<Literal> literals = new ArrayList<>();
        String[] splits = line.split("\\s+");
        for (String split : splits) {
            split = split.toLowerCase();
            String CONJUNCTION = "v";
            if (split.equals(CONJUNCTION)) {
                continue;
            }
            String NEGATION = "~";
            if (split.startsWith(NEGATION)) {
                Literal lit = new Literal(split.replace("~", ""), true);
                literals.add(lit);
            } else {
                literals.add(new Literal(split, false));
            }
        }

        return new Clause(literals);
    }

}
