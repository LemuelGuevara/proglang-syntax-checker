package parser.parser;

import parser.lexer.Lexer;
import parser.lexer.Token;
import parser.lexer.TokenType;

import java.util.ArrayList;
import java.util.List;

public abstract class Parser {
  protected Lexer lexer;
  protected Token currentToken;
  protected int assignmentVariableCounter = 0;
  String[] declaredVariables = new String[100];
  int variableCount = 0;
  protected List<Token> validTokens = new ArrayList<>();

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
      TokenType dataType;

      if (isValidDataType()) {
        dataType = currentToken.getType();
        analyzeNextToken(currentToken.getType());

        if (currentToken.getType().equals(TokenType.IDENTIFIER)) {
          analyzeNextToken(currentToken.getType());

          if (currentToken.getType().equals(TokenType.ASSIGN)) {
            analyzeNextToken(currentToken.getType());
            TokenType valueTokenType = currentToken.getType();

            if (isMatchingTypeValue(dataType, valueTokenType)) {
              analyzeNextToken(currentToken.getType());
              if (hasSemicolon()) {
                assignmentVariableCounter++;
                System.out.println("Valid variable assignment found: " + dataType);
              }
            } else {
              System.out.println("Invalid assignment: type mismatch");
            }
          }
        }
      } else {
        analyzeNextToken(currentToken.getType());  // Skip non-assignment tokens
      }
    }
    return false;
  }

  protected boolean isMatchingTypeValue(TokenType dataType, TokenType valueType) {
    if (dataType == null || valueType == null) {
      System.out.println("Null value encountered in type matching - DataType: " + dataType + ", ValueType: " + valueType);
      return false;
    }

    return (dataType.equals(TokenType.TYPE_INT) && valueType.equals(TokenType.INTEGER_LITERAL)) ||
            (dataType.equals(TokenType.TYPE_DOUBLE) && valueType.equals(TokenType.DOUBLE_LITERAL)) ||
            (dataType.equals(TokenType.TYPE_FLOAT) && valueType.equals(TokenType.FLOAT_LITERAL)) ||
            (dataType.equals(TokenType.TYPE_BOOLEAN) && valueType.equals(TokenType.BOOLEAN_LITERAL)) ||
            (dataType.equals(TokenType.TYPE_BYTE) && valueType.equals(TokenType.BYTE_LITERAL)) ||
            (dataType.equals(TokenType.TYPE_LONG) && valueType.equals(TokenType.LONG_LITERAL)) ||
            (dataType.equals(TokenType.TYPE_SHORT) && valueType.equals(TokenType.SHORT_LITERAL));
  }

  protected boolean isValidDataType() {
    return currentToken.getType().equals(TokenType.TYPE_INT) ||
            currentToken.getType().equals(TokenType.TYPE_DOUBLE) ||
            currentToken.getType().equals(TokenType.TYPE_FLOAT) ||
            currentToken.getType().equals(TokenType.TYPE_BYTE) ||
            currentToken.getType().equals(TokenType.TYPE_SHORT) ||
            currentToken.getType().equals(TokenType.TYPE_LONG) ||
            currentToken.getType().equals(TokenType.TYPE_BOOLEAN) ||
            currentToken.getType().equals(TokenType.TYPE_CHAR);
  }

  // Method to check for specific tokens or conditions, can be extended further
  protected abstract void runChecks();
}
