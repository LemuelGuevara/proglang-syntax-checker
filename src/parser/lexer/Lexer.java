package parser.lexer;

import java.util.function.Predicate;

public class Lexer {
    private final String input;
    private int position;
    private char currentChar;

    public Lexer(String input) {
        this.input = input;
        this.position = 0;
        this.currentChar = input.charAt(position);
        System.out.println("Lexer initialized with input: " + input); // Debugging line
    }

    public void reset() {
        position = 0; // Reset the position to the start
        if (!input.isEmpty()) {
            currentChar = input.charAt(position); // Reset the current character
        }
    }

    public Token getNextToken() {
        while (currentChar != '\0') {

            if (Character.isWhitespace(currentChar)) {
                skipWhitespace();
                continue;
            }

            // Check for identifiers or keywords
            if (Character.isLetter(currentChar)) {
                return getLiterals(); // Handle keywords and identifiers
            }

            // Check for digits
            if (Character.isDigit(currentChar)) {
                String number = readLiteral(Character::isDigit);
                return new Token(TokenType.INTEGER_LITERAL, number);
            }

            // Handle delimiters and operators
            switch (currentChar) {
                case '(':
                    advance(); // Advance to the next character after returning the token
                    return new Token(TokenType.LEFT_PARENT, "(");
                case ')':
                    advance();
                    return new Token(TokenType.RIGHT_PARENT, ")");
                case '{':
                    advance();
                    return new Token(TokenType.LEFT_BRACE, "{");
                case '}':
                    advance();
                    return new Token(TokenType.RIGHT_BRACE, "}");
                case ';':
                    advance();
                    return new Token(TokenType.SEMICOLON, ";");
                case ',':
                    advance();
                    return new Token(TokenType.COMMA, ",");
                case '+':
                    advance(); // Consume the first '+'
                    if (currentChar == '+') {
                        advance(); // Consume the second '+'
                        return new Token(TokenType.INCREMENT, "++"); // Return the '++' token
                    }
                    // If the next character is not another '+', return a single '+' token
                    return new Token(TokenType.OPERATOR, "+");
                case '-':
                    advance(); // Consume the first '-'
                    if (currentChar == '-') {
                        advance(); // Consume the second '-'
                        return new Token(TokenType.DECREMENT, "--"); // Return the '--' token
                    }
                    // If the next character is not another '-', return a single '-' token
                    return new Token(TokenType.OPERATOR, "-");
                case '*':
                    advance();
                    return new Token(TokenType.OPERATOR, "*");
                case '/':
                    advance();
                    return new Token(TokenType.OPERATOR, "/");
                case '<':
                    advance();
                    if (currentChar == '=') {
                        advance();
                        return new Token(TokenType.LESS_EQUAL, "<=");
                    }
                    return new Token(TokenType.LESS_THAN, "<");
                case '>':
                    advance();
                    if (currentChar == '=') {
                        advance();
                        return new Token(TokenType.GREATER_EQUAL, ">=");
                    }
                    return new Token(TokenType.GREATER_THAN, ">");
                case '=':
                    advance();
                    return new Token(TokenType.ASSIGN, "=");
                default:
                    throw new RuntimeException("Invalid character: " + currentChar);
            }
        }

        return new Token(TokenType.EOF, null);
    }

    // Returns a token for identifiers and keywords
    private Token getLiterals() {
        String identifier = readLiteral(Character::isLetterOrDigit); // Allow letters and digits
        switch (identifier) {
            case "for":
                return new Token(TokenType.KEYWORD_FOR, identifier);
            case "int":
                return new Token(TokenType.KEYWORD_INT, identifier);
            case "double":
                return new Token(TokenType.KEYWORD_DOUBLE, identifier);
            default:
                return new Token(TokenType.IDENTIFIER, identifier);
        }
    }

    private void advance() {
        position++;
        if (position >= input.length()) {
            currentChar = '\0';  // End of input
        } else {
            currentChar = input.charAt(position);
        }
    }

    private void skipWhitespace() {
        if (position < input.length() && Character.isWhitespace(currentChar)) {
            advance();
            skipWhitespace(); // Recursive call to skip any remaining whitespace
        }
    }

    private String readLiteral(Predicate<Character> condition) {
        StringBuilder result = new StringBuilder();
        while (position < input.length() && condition.test(currentChar)) {
            result.append(currentChar);
            advance(); // Move to the next character
        }
        return result.toString();
    }

    // Method to print all tokens for debugging
    public void printTokens() {
        Token token = getNextToken();
        while (token.getType() != TokenType.EOF) {
            System.out.println(token);
            token = getNextToken();
        }
    }
}
