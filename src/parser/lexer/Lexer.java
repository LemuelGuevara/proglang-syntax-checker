package parser.lexer;

import parser.parser.DataTypeParser;

import java.util.function.Predicate;

public class Lexer {
    private final String input;
    private int position;
    private char currentChar;
    private final DataTypeParser dataTypeParser = new DataTypeParser();
    private TokenType lastTypeContext = null;

    public Lexer(String input) {
        this.input = input;
        this.position = 0;
        this.currentChar = input.charAt(position);
        System.out.println("Lexer initialized with input: " + input);
    }

    public void reset() {
        position = 0;
        if (!input.isEmpty()) {
            currentChar = input.charAt(position);
        }
    }

    public Token getNextToken() {
        while (currentChar != '\0') {

            if (Character.isWhitespace(currentChar)) {
                skipWhitespace();
                continue;
            }

            if (Character.isLetter(currentChar)) {
                Token token = getLiterals();
                if (token.getType().equals(TokenType.TYPE_BYTE) ||
                        token.getType().equals(TokenType.TYPE_SHORT) ||
                        token.getType().equals(TokenType.TYPE_INT) ||
                        token.getType().equals(TokenType.TYPE_CHAR)) {
                    lastTypeContext = token.getType();
                }
                return token;
            }

            if (lastTypeContext != null && lastTypeContext.equals(TokenType.TYPE_CHAR)) {
                if (currentChar == '\'') {
                    advance();

                    if (currentChar == '\'')
                        throw new RuntimeException("Invalid character literal: empty character literal");

                    char charValue = currentChar;
                    advance();
                    if (currentChar != '\'')
                        throw new RuntimeException("Invalid character literal: missing closing quote");
                    advance();
                    return new Token(TokenType.CHAR_LITERAL, String.valueOf(charValue));
                }
            }

            // Check for digits
            if (Character.isDigit(currentChar)) {
                StringBuilder number = new StringBuilder(readLiteral(Character::isDigit));

                if (currentChar == '.') {
                    advance();

                    if (Character.isDigit(currentChar)) {
                        String fractionalPart = readLiteral(Character::isDigit);
                        number.append('.').append(fractionalPart);
                        return new Token(TokenType.DOUBLE_LITERAL, number.toString());
                    } else {
                        throw new RuntimeException("Invalid double literal: missing fractional part after decimal");
                    }
                }

                if (currentChar == 'f' || currentChar == 'F') {
                    number.append(currentChar);
                    advance();
                    return new Token(TokenType.FLOAT_LITERAL, number.toString());
                }

                if (currentChar == 'l' || currentChar == 'L') {
                    number.append(currentChar);
                    advance();
                    return new Token(TokenType.LONG_LITERAL, number.toString());
                }

                int value = Integer.parseInt(number.toString());
                if (lastTypeContext.equals(TokenType.TYPE_BYTE) && dataTypeParser.isByte(value))
                    return new Token(TokenType.BYTE_LITERAL, String.valueOf(value));
                if (lastTypeContext.equals(TokenType.TYPE_SHORT) && dataTypeParser.isShort(value))
                    return new Token(TokenType.SHORT_LITERAL, String.valueOf(value));

                return new Token(TokenType.INTEGER_LITERAL, number.toString());
            }

            // Handle delimiters and operators
            switch (currentChar) {
                case '(':
                    advance();
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
                    advance();
                    if (currentChar == '+') {
                        advance();
                        return new Token(TokenType.INCREMENT, "++");
                    }
                    return new Token(TokenType.OPERATOR, "+");
                case '-':
                    advance();
                    if (currentChar == '-') {
                        advance();
                        return new Token(TokenType.DECREMENT, "--");
                    }
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
        String identifier = readLiteral(ch -> Character.isLetterOrDigit(ch) || ch == '.');
        switch (identifier) {
            case "for":
                return new Token(TokenType.KEYWORD_FOR, identifier);
            case "int":
                return new Token(TokenType.TYPE_INT, identifier);
            case "float":
                return new Token(TokenType.TYPE_FLOAT, identifier);
            case "double":
                return new Token(TokenType.TYPE_DOUBLE, identifier);
            case "byte":
                return new Token(TokenType.TYPE_BYTE, identifier);
            case "short":
                return new Token(TokenType.TYPE_SHORT, identifier);
            case "long":
                return new Token(TokenType.TYPE_LONG, identifier);
            case "boolean":
                return new Token(TokenType.TYPE_BOOLEAN, identifier);
            case "true":
            case "false":
                return new Token(TokenType.BOOLEAN_LITERAL, identifier);
            case "char":
                return new Token(TokenType.TYPE_CHAR, identifier);
            case "System.out.println":
                return new Token(TokenType.SYSTEM_PRINTLN_OUT, identifier);
            default:
                return new Token(TokenType.IDENTIFIER, identifier);
        }
    }

    private void advance() {
        position++;
        if (position >= input.length())
            currentChar = '\0';
        else
            currentChar = input.charAt(position);
    }

    private void skipWhitespace() {
        if (position < input.length() && Character.isWhitespace(currentChar)) {
            advance();
            skipWhitespace();
        }
    }

    private String readLiteral(Predicate<Character> condition) {
        StringBuilder result = new StringBuilder();
        while (position < input.length() && condition.test(currentChar)) {
            result.append(currentChar);
            advance();
        }
        return result.toString();
    }
}
