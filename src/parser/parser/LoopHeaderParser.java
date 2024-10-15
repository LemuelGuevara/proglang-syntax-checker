package parser.parser;

import parser.lexer.Lexer;
import parser.lexer.Token;
import parser.lexer.TokenType;

public class LoopHeaderParser extends Parser {
    private Token initializationToken;

    public LoopHeaderParser(Lexer lexer) {
        super(lexer);
    }

    private boolean findSemicolon(int semicolonIndex) {
        resetLexer();

        int foundCount = 0;

        while (currentToken.getType() != TokenType.EOF) {
            if (currentToken.getType().equals(TokenType.SEMICOLON)) {
                foundCount++;
                if (foundCount == semicolonIndex) {
                    currentToken = lexer.getNextToken();
                    return true;
                }
            }
            currentToken = lexer.getNextToken();
        }

        return false;
    }

    private boolean hasCorrectHeaderDelimiters() {
        // Check for the first semicolon
        if (!findSemicolon(1)) return false;
        // Find the second semicolon
        return findSemicolon(2);
    }

    private boolean hasCorrectParents() {
        resetLexer();
        int parentCount = 0;
        int braceCount = 0;

        while (currentToken.getType() != TokenType.EOF) {
            switch (currentToken.getType()) {
                case LEFT_PARENT:
                    parentCount++;
                    break;
                case RIGHT_PARENT:
                    parentCount--;
                    if (parentCount < 0) return false; // Unmatched right parenthesis
                    break;
                case LEFT_BRACE:
                    braceCount++;
                    break;
                case RIGHT_BRACE:
                    braceCount--;
                    if (braceCount < 0) return false; // Unmatched right brace
                    break;
            }
            analyzeNextToken(currentToken.getType()); // Move to the next token
        }

        return parentCount == 0 && braceCount == 0;
    }

    private boolean isCorrectSequence() {
        resetLexer();
        boolean foundKeywordFor = false;
        boolean foundLeftParent = false;
        boolean foundRightParent = false;
        boolean foundLeftBrace = false;
        boolean foundRightBrace = false;

        while (currentToken.getType() != TokenType.EOF) {
            if (currentToken.getType().equals(TokenType.KEYWORD_FOR))
                foundKeywordFor = true;
            if (currentToken.getType().equals(TokenType.LEFT_PARENT))
                foundLeftParent = true;
            if (foundLeftParent && currentToken.getType().equals(TokenType.RIGHT_PARENT))
                foundRightParent = true;
            if (currentToken.getType().equals(TokenType.LEFT_BRACE))
                foundLeftBrace = true;
            if (currentToken.getType().equals(TokenType.RIGHT_BRACE))
                foundRightBrace = true;
            analyzeNextToken(currentToken.getType());
        }

        boolean foundParents = foundLeftParent && foundRightParent;
        boolean foundBraces = foundLeftBrace && foundRightBrace;

        return foundKeywordFor && foundParents && foundBraces && hasCorrectParents();
    }

    private boolean isCorrectInitialization() {
        resetLexer();
        boolean foundIntKeyword = false;
        boolean foundIdentifier = false;
        boolean foundAssign = false;
        boolean foundIntegerLiteral = false;

        while (currentToken.getType() != TokenType.EOF) {
            if (currentToken.getType() == TokenType.SEMICOLON) break;
            if (currentToken.getType().equals(TokenType.TYPE_INT))
                foundIntKeyword = true;
            if (currentToken.getType().equals(TokenType.IDENTIFIER)) {
                foundIdentifier = true;
                initializationToken = currentToken;
            }
            if (currentToken.getType().equals(TokenType.ASSIGN))
                foundAssign = true;
            if (currentToken.getType().equals(TokenType.INTEGER_LITERAL) && foundAssign)
                foundIntegerLiteral = true;

            analyzeNextToken(currentToken.getType());
        }
        return foundIntKeyword && foundIdentifier && foundAssign && foundIntegerLiteral;
    }

    private boolean isCorrectCondition() {
        resetLexer();
        findSemicolon(1);

        boolean foundIdentifier = false;
        boolean foundOperand = false;
        boolean foundGreaterThan = false;
        boolean foundLessThan = false;
        boolean foundGreaterEqual = false;
        boolean foundLessEqual = false;

        while (currentToken.getType() != TokenType.EOF) {
            // Check for identifiers
            if (currentToken.getType().equals(TokenType.IDENTIFIER)) {
                if (foundIdentifier) foundOperand = true;
                else foundIdentifier = true;
            }

            // Check for integer literals
            if (currentToken.getType().equals(TokenType.INTEGER_LITERAL))
                foundOperand = true;

            // Check for relational operators
            if (currentToken.getType().equals(TokenType.GREATER_THAN))
                foundGreaterThan = true;
            if (currentToken.getType().equals(TokenType.GREATER_EQUAL))
                foundGreaterEqual = true;
            if (currentToken.getType().equals(TokenType.LESS_EQUAL))
                foundLessEqual = true;
            if (currentToken.getType().equals(TokenType.LESS_THAN))
                foundLessThan = true;

            analyzeNextToken(currentToken.getType());

            if (currentToken.getType() == TokenType.SEMICOLON) break;
        }

        return foundIdentifier && foundOperand &&
                (foundGreaterThan || foundLessThan || foundGreaterEqual || foundLessEqual);
    }

    private boolean isCorrectIncrementDecrement() {
        if (findSemicolon(2)) {
            boolean matchingInitialization = false;
            boolean foundValidIncrement = false;
            boolean foundValidDecrement = false;

            while (currentToken.getType() != TokenType.EOF) {
                if (currentToken.getType() == TokenType.RIGHT_BRACE) break;
                if (currentToken.getValue().equals(initializationToken.getValue()))
                    matchingInitialization = true;

                if (currentToken.getType().equals(TokenType.INCREMENT)) {
                    foundValidIncrement = true; // Accept ++i
                } else if (currentToken.getType().equals(TokenType.DECREMENT)) {
                    foundValidDecrement = true; // Accept --i
                }

                // Check for valid increment/decrement patterns
                if (currentToken.getType().equals(TokenType.INCREMENT) || currentToken.getType().equals(TokenType.DECREMENT)) {
                    foundValidIncrement = currentToken.getType().equals(TokenType.INCREMENT);
                    foundValidDecrement = currentToken.getType().equals(TokenType.DECREMENT);
                }

                // Check for invalid increment/decrement patterns
                if (currentToken.getType() == TokenType.OPERATOR &&
                        (currentToken.getValue().equals("+") || currentToken.getValue().equals("-"))) {
                    Token nextToken = lexer.getNextToken();
                    if (nextToken.getType() == TokenType.INCREMENT || nextToken.getType() == TokenType.DECREMENT) {
                        foundValidIncrement = nextToken.getType().equals(TokenType.INCREMENT);
                        foundValidDecrement = nextToken.getType().equals(TokenType.DECREMENT);
                    } else return false;
                }

                // Break out of the loop if a valid increment or decrement is found
                if (foundValidIncrement || foundValidDecrement) break;

                currentToken = lexer.getNextToken();
            }
            return matchingInitialization && (foundValidIncrement || foundValidDecrement);
        }
        return false;
    }

    public void runChecks() {
        System.out.println("\nCHECKING LOOP HEADER...");

        // Checking for delimiters, parenthesis, and curly braces
        if (hasCorrectParents()) System.out.println("Balanced parents");
        else System.out.println("Unbalanced parents");
        // Checking for correct delimiters
        if (hasCorrectHeaderDelimiters()) System.out.println("Correct and complete header delimiters");
        else System.out.println("Incorrect and incomplete header delimiters");
        // Checking for correct sequence
        if (isCorrectSequence()) System.out.println("Correct () {} sequence");
        else System.out.println("Incorrect () {} sequence");

        System.out.println();
        // Checks for initialization
        if (isCorrectInitialization()) System.out.println("Found correct initialization");
        else System.out.println("Incorrect initialization");

        // Checks for condition
        if (isCorrectCondition()) System.out.println("Found correct condition");
        else System.out.println("Incorrect condition");

        // Check for increment/decrement
        if (isCorrectIncrementDecrement()) System.out.println("Found correct increment decrement");
        else System.out.println("Incorrect increment decrement");
    }
}