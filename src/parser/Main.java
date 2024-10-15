package parser;

import parser.lexer.Lexer;
import parser.parser.LoopBodyParser;
import parser.parser.LoopHeaderParser;

public class Main {
    public static void main(String[] args) {
        // Array of for loop scenario
        String[] forLoops = {
                "for (int i = 0; i < 10; i++) {" +
                        "System.out.println();" +
                        "System.out.println();" +
                        "int i = 0;" +
                        "boolean test = false;" +
                        "short test = 10;" +
                        "long test = 10L;" +
                        "float test  = 10f;" +
                        "double test = 10.1;" +
                        "char test = 'c'" +
                        "}",
                /* "for (int i = 0; i < 10; i++) {}",             // Valid loop
                 "for (int i = 0; i <= n; i++) {}",             // Invalid: incorrect comparison operator
                 "for (int i = 0; i < ; i++) {}",                // Invalid: missing condition
                 "for (int i = 0; i < n + 5; i++) {}",           // Valid loop
                 "for (int i = 0; i < a; i++) {}",               // Valid loop with identifier
                 "for (int i = 0; i < 10 + b; i++) {}",          // Valid loop with integer and identifier in condition
                 "for (int i = 0; i < n; i+1++) {}",             // Invalid: incorrect increment operator
                 "for (int i = 0; i < 10; ) {}",                 // Invalid: missing increment expression
                 "for (; i < n; i++) {}"                          // Valid loop with missing initialization*/
        };

        // Iterate through each for loop scenario
        for (String forLoop : forLoops) {
            System.out.println("Testing: " + forLoop);
            Lexer lexer = new Lexer(forLoop);
            LoopHeaderParser loopHeaderParser = new LoopHeaderParser(lexer);
            LoopBodyParser loopBodyParser = new LoopBodyParser(lexer);

            loopHeaderParser.runChecks();
            loopBodyParser.runChecks();
            System.out.println();
        }
    }
}

