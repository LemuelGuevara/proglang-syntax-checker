package parser.lexer;

public enum TokenType {
    // Keywords
    KEYWORD_FOR,

    TYPE_INT,
    TYPE_FLOAT,
    TYPE_DOUBLE,
    TYPE_BYTE,
    TYPE_SHORT,
    TYPE_LONG,
    TYPE_BOOLEAN,
    TYPE_CHAR,

    // Identifiers and literals
    IDENTIFIER,
    INTEGER_LITERAL,
    FLOAT_LITERAL,
    DOUBLE_LITERAL,
    BYTE_LITERAL,
    SHORT_LITERAL,
    LONG_LITERAL,
    BOOLEAN_LITERAL,
    CHAR_LITERAL,
    SYSTEM_PRINT_OUT,
    SYSTEM_PRINTLN_OUT,

    // Operators
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
