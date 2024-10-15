package parser.parser;

import parser.lexer.Lexer;
import parser.lexer.Token;
import parser.lexer.TokenType;

public abstract class Parser {
    protected Lexer lexer;
    protected Token currentToken;
    protected int assignmentVariableCounter = 0;

    protected Parser(Lexer lexer) {
        this.lexer = lexer;
        this.currentToken = lexer.getNextToken();
    }

    protected void analyzeNextToken(TokenType expectedType) {
        if (currentToken.getType() != expectedType) System.out.println("Invalid token type");
        currentToken = lexer.getNextToken();
    }

    // Method to reset the lexer
    protected void resetLexer() {
        lexer.reset();
        currentToken = lexer.getNextToken();
    }

    protected boolean hasSemicolon() {
        if (currentToken.getType().equals(TokenType.SEMICOLON)) {
            analyzeNextToken(currentToken.getType());
            return true;
        }
        return false;
    }

    protected boolean isValidVariableAssignment() {
        while (currentToken.getType() != TokenType.EOF) {
            if (isValidDataType()) analyzeNextToken(currentToken.getType());
            if (currentToken.getType().equals(TokenType.IDENTIFIER))
                analyzeNextToken(currentToken.getType());

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

    protected boolean isValidDataType() {
        return currentToken.getType().equals(TokenType.TYPE_INT) ||
                currentToken.getType().equals(TokenType.TYPE_FLOAT) ||
                currentToken.getType().equals(TokenType.TYPE_BYTE) ||
                currentToken.getType().equals(TokenType.TYPE_SHORT) ||
                currentToken.getType().equals(TokenType.TYPE_LONG) ||
                currentToken.getType().equals(TokenType.TYPE_BOOLEAN) ||
                currentToken.getType().equals(TokenType.TYPE_CHAR);
    }

    protected boolean isValidValue() {
        return currentToken.getType().equals(TokenType.INTEGER_LITERAL) ||
                currentToken.getType().equals(TokenType.DOUBLE_LITERAL) ||
                currentToken.getType().equals(TokenType.FLOAT_LITERAL) ||
                currentToken.getType().equals(TokenType.BYTE_LITERAL) ||
                currentToken.getType().equals(TokenType.SHORT_LITERAL) ||
                currentToken.getType().equals(TokenType.LONG_LITERAL) ||
                currentToken.getType().equals(TokenType.BOOLEAN_LITERAL) ||
                currentToken.getType().equals(TokenType.CHAR_LITERAL);
    }

    // Method to check for specific tokens or conditions, can be extended further
    protected abstract void runChecks();
}
