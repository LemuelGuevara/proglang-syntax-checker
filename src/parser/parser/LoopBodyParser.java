package parser.parser;

import parser.lexer.Lexer;
import parser.lexer.Token;

public class LoopBodyParser extends Parser {
    private Token currentToken;

    public LoopBodyParser(Lexer lexer) {
        super(lexer);
    }

    @Override
    public void runChecks() {

    }
}
