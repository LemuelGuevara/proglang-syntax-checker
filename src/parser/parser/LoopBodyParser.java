package parser.parser;

import parser.lexer.Lexer;
import parser.lexer.TokenType;

public class LoopBodyParser extends Parser {
    private int sysOutCounter = 0;

    public LoopBodyParser(Lexer lexer) {
        super(lexer);
    }

    private boolean isValidSystemOutPrintln() {
        if (currentToken.getType().equals(TokenType.SYSTEM_PRINTLN_OUT)) {
            sysOutCounter++;
            analyzeNextToken(currentToken.getType());

            if (currentToken.getType().equals(TokenType.LEFT_PARENT)) {
                analyzeNextToken(currentToken.getType());

                if (currentToken.getType().equals(TokenType.RIGHT_PARENT)) {
                    analyzeNextToken(currentToken.getType());
                    return hasSemicolon();
                }
            }
        }
        return false;
    }

    private boolean isValidLoopBody() {
        boolean validStatementFound = false;

        while (currentToken.getType() != TokenType.EOF) {
            if (currentToken.getType().equals(TokenType.LEFT_BRACE)) {
                analyzeNextToken(currentToken.getType());

                while (currentToken.getType() != TokenType.RIGHT_BRACE && currentToken.getType() != TokenType.EOF) {
                    boolean foundStatement = false;

                    while (isValidSystemOutPrintln()) {
                        System.out.println("Valid System.out.println found in loop body.");
                        foundStatement = true;
                    }

                    if (isValidVariableAssignment()) {
                        System.out.println("Valid variable assignment found in loop body.");
                        foundStatement = true;
                    }

                    validStatementFound |= foundStatement;
                    analyzeNextToken(currentToken.getType());
                }

                if (currentToken.getType().equals(TokenType.RIGHT_BRACE)) {
                    analyzeNextToken(currentToken.getType());
                    return validStatementFound;
                }
            }
            analyzeNextToken(currentToken.getType());
        }
        return validStatementFound;
    }

    @Override
    public void runChecks() {
        System.out.println("\nCHECKING LOOP BODY...");
        if (isValidLoopBody()) {
            System.out.println();
            System.out.println("Number of valid sysout statements found: " + sysOutCounter);
            System.out.println("Number of valid assignments found: " + assignmentVariableCounter);
            System.out.println("Valid (non-empty) loop body detected.");
        } else {
            System.out.println("No valid (non-empty) loop body found.");
        }
    }
}
