package parser.parser;

import parser.lexer.Lexer;
import parser.lexer.Token;
import parser.lexer.TokenType;

public abstract class Parser {
    protected Lexer lexer;
    protected Token currentToken;

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

    // Method to check for specific tokens or conditions, can be extended further
    protected abstract void runChecks();
}
