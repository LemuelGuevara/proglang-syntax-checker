package parser;

import parser.lexer.Lexer;
import parser.parser.LoopBodyParser;
import parser.parser.LoopHeaderParser;

public class Main {
    public static void main(String[] args) {
        String correctSampleRun = "for (int i = 0; i < 1; i++) {" +
                "System.out.println();" +
                "System.out.println();" +
                "int i = 0;" +
                "boolean test = false;" +
                "short test = 10;" +
                "long test = 10L;" +
                "float test = 10f;" +
                "double test = 10.1;" +
                "}";
        // Array of for loop header scenarios
        String[] forLoopsHeaders = {
                "for (int i = 0 i < 10 i++) {}",
                "for (int 1i = 0; i < 10; i++) {}",
                "for (int i = 0; i = 10; i++) {}",
                "for (int i = 0; i < 10; i+) {}",
        };

        // Array of for loop body scenarios
        String[] forLoopsBodies = {
                "for (int i = 0; i < 1; i++) {" +
                        "System.out.println();" +
                        "System.out.println();" +
                        "boolean j = false;" +
                        "short k = 10;" +
                        "long l = 10L;" +
                        "float m = 10f;" +
                        "double n = 10.1;" +
                        "byte o = 126;" +
                        "}",
                "for (int i = 0; i < 1; i++) {" +
                        "boolean j = test;" +
                        "short k = string;" +
                        "long l = 1.1;" +
                        "float m = 10L" +
                        "double n = 10F;" +
                        "}",
                "for (int i = 0; i < 1; i++) {" +
                        "System.out.println();" +
                        "System.out.print();" +
                        "System.out.;" +
                        "System.out.println);" +
                        "}",
        };

       /* System.out.println("Correct sample run: ");
        Lexer lexer = new Lexer(correctSampleRun);
        LoopBodyParser loopBodyParser = new LoopBodyParser(lexer);
        LoopHeaderParser loopHeaderParser = new LoopHeaderParser(lexer);
        loopHeaderParser.runChecks();
        loopBodyParser.runChecks();*/

        // Testing loop headers
        System.out.println("Testing Loop Headers:");
        for (String forLoopHeader : forLoopsHeaders) {
            System.out.println("Testing: " + forLoopHeader);
            Lexer lexer = new Lexer(forLoopHeader);
            LoopHeaderParser loopHeaderParser = new LoopHeaderParser(lexer);
            loopHeaderParser.runChecks();
            System.out.println();
        }

        // Testing loop bodies
        System.out.println("Testing Loop Bodies:");
        for (String forLoopBody : forLoopsBodies) {
            System.out.println("Testing: " + forLoopBody);
            Lexer lexer = new Lexer(forLoopBody);
            LoopBodyParser loopBodyParser = new LoopBodyParser(lexer);
            loopBodyParser.runChecks();
            System.out.println();
        }
    }
}
