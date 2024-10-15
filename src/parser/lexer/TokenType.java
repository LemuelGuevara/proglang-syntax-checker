package parser.lexer;

public enum TokenType {
    // Keywords
    KEYWORD_FOR,
    KEYWORD_INT,
    KEYWORD_FLOAT,
    KEYWORD_DOUBLE,

    // Identifiers and literals
    IDENTIFIER,
    INTEGER_LITERAL,
    FLOAT_LITERAL,

    // Operators
    PLUS,
    MINUS,
    MULTIPLY,
    DIVIDE,
    ASSIGN,
    LESS_THAN,
    GREATER_THAN,
    GREATER_EQUAL,
    LESS_EQUAL,
    NOT_EQUAL,
    OPERATOR,

    // Increment/Decrement
    INCREMENT,
    DECREMENT,

    // Delimiters
    LEFT_PARENT,
    RIGHT_PARENT,
    LEFT_BRACE,
    RIGHT_BRACE,
    SEMICOLON,
    COMMA,

    // Special tokens
    EOF  // End of file
}
