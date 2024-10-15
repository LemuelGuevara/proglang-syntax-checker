package parser.parser;

import parser.lexer.Lexer;
import parser.lexer.Token;

public abstract class Parser {
    protected Lexer lexer;
    protected Token currentToken;

    protected Parser(Lexer lexer) {
        this.lexer = lexer;
        this.currentToken = lexer.getNextToken();
    }

    // Method to fetch the next token
    protected Token getNextToken() {
        currentToken = lexer.getNextToken();
        return currentToken;
    }

    // Method to reset the lexer
    protected void resetLexer() {
        lexer.reset();
        currentToken = lexer.getNextToken();
    }

    // Method to check for specific tokens or conditions, can be extended further
    protected abstract void runChecks();
}
