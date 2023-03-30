package ui;

import ui.algorithms.CookingHelper;
import ui.algorithms.RefutationResolution;
import ui.clauses.Clauses;
import ui.clauses.UserInputs;
import ui.parser.InputFileParser;

import java.nio.file.Files;
import java.nio.file.Path;

public class Solution {

    public static void main(String[] args) {
        if (args.length < 2 || args.length > 3) {
            throw new IllegalArgumentException("Wrong number of arguments passed!");
        }
        String RESOLUTION = "resolution";
        String COOKING = "cooking";
        if (args[0].equalsIgnoreCase(RESOLUTION) &&
            args.length == 2 &&
            Files.exists(Path.of(args[1]))) {

            Clauses clauses = InputFileParser.parseClauseFile(args[1]);
            RefutationResolution.resolution(clauses);


        } else if(args[0].equals(COOKING) &&
            args.length == 3 &&
            Files.exists(Path.of(args[1])) &&
            Files.exists(Path.of(args[2]))) {

            Clauses clauses = InputFileParser.parseClauseFile(args[1]);
            UserInputs inputs = InputFileParser.parseUserInputs(args[2]);
            CookingHelper.cook(clauses, inputs);
        }
    }
}
