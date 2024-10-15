package parser.parser;

import parser.lexer.Lexer;
import parser.lexer.TokenType;

public class LoopBodyParser extends Parser {
    private int sysOutCounter = 0;
    private int assignmentVariableCounter = 0;

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

    private boolean isValidVariableAssignment() {
        while (currentToken.getType() != TokenType.EOF) {
            if (isValidDataType()) analyzeNextToken(currentToken.getType());
            if (currentToken.getType().equals(TokenType.IDENTIFIER)) analyzeNextToken(currentToken.getType());

            if (currentToken.getType().equals(TokenType.ASSIGN)) {
                analyzeNextToken(currentToken.getType());

                if (isValidValue()) {
                    analyzeNextToken(currentToken.getType());
                    if (hasSemicolon()) {
                        assignmentVariableCounter++;
                        return true;
                    } else {
                        System.out.println("Invalid assignment variable found");
                        return false;
                    }
                }
            }
            analyzeNextToken(currentToken.getType());
        }
        return false;
    }


    private boolean isValidDataType() {
        return currentToken.getType().equals(TokenType.TYPE_INT) ||
                currentToken.getType().equals(TokenType.TYPE_FLOAT) ||
                currentToken.getType().equals(TokenType.TYPE_BYTE) ||
                currentToken.getType().equals(TokenType.TYPE_SHORT) ||
                currentToken.getType().equals(TokenType.TYPE_LONG) ||
                currentToken.getType().equals(TokenType.TYPE_BOOLEAN) ||
                currentToken.getType().equals(TokenType.TYPE_CHAR);
    }

    private boolean isValidValue() {
        return currentToken.getType().equals(TokenType.INTEGER_LITERAL) ||
                currentToken.getType().equals(TokenType.DOUBLE_LITERAL) ||
                currentToken.getType().equals(TokenType.FLOAT_LITERAL) ||
                currentToken.getType().equals(TokenType.BYTE_LITERAL) ||
                currentToken.getType().equals(TokenType.SHORT_LITERAL) ||
                currentToken.getType().equals(TokenType.LONG_LITERAL) ||
                currentToken.getType().equals(TokenType.BOOLEAN_LITERAL) ||
                currentToken.getType().equals(TokenType.CHAR_LITERAL);
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
