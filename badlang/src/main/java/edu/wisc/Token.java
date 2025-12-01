package edu.wisc;

// Represents a single token produced by the lexer.
public class Token {
  public enum TokenType {
    // Keywords
    INT, BOOL, FUN, IF, ELSE, WHILE, RETURN, PRINT, TRUE, FALSE,

    // Single-character punctuation
    LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE,
    COMMA, SEMICOLON, EQUAL,

    // Operators
    PLUS, MINUS, STAR, SLASH,
    EQUAL_EQUAL, NOT_EQUAL, GREATER, GREATER_EQUAL, LESS, LESS_EQUAL,
    AND, OR, NOT,

    // Literals
    IDENTIFIER, NUMBER,

    // Special
    EOF
  }

  private final TokenType type;
  private final String lexeme;
  private final Object literal;
  private final int line;
  public Token(TokenType type, String lexeme, Object literal, int line) { this.type = type; this.lexeme = lexeme; this.literal = literal; this.line = line; }
  public TokenType getType() { return type; }
  public String getLexeme() { return lexeme; }
  public Object getLiteral() { return literal; }
  public int getLine() { return line; }

  @Override public String toString() { return String.format("Token(type = %-15s, lexeme = %-15s, literal = %-10s, line = %10d)", type, lexeme, literal, line); }
}