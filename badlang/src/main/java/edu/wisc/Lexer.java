package edu.wisc;
import java.util.*;

/* The Lexer (also known as Scanner or Tokenizer) converts source code into tokens.
 * 1. Read the source code character by character
 * 2. Recognize all tokens defined in the language (keywords, identifiers, literals, operators, etc.)
 * 3. Handle whitespace appropriately
 * 4. Report errors with meaningful messages
 * 5. Track line numbers for error reporting */
public class Lexer {
  private static final Map<String, Token.TokenType> keywords = new HashMap<>();
  static { // put all the keywords in
    keywords.put("int",    Token.TokenType.INT);
    keywords.put("bool",   Token.TokenType.BOOL);
    keywords.put("fun",    Token.TokenType.FUN);
    keywords.put("if",     Token.TokenType.IF);
    keywords.put("else",   Token.TokenType.ELSE);
    keywords.put("while",  Token.TokenType.WHILE);
    keywords.put("return", Token.TokenType.RETURN);
    keywords.put("print",  Token.TokenType.PRINT);
    keywords.put("true",   Token.TokenType.TRUE);
    keywords.put("false",  Token.TokenType.FALSE);
  }
  private final String source;
  private final List<Token> tokens = new ArrayList<>();
  private int start = 0, current = 0, line = 1;
  public Lexer(String source) { this.source = source; }

  public List<Token> scanTokens() { // loop through character one by one
    while (!isAtEnd()) {
      start = current;
      scanToken();
      current++;
    }
    tokens.add(new Token(Token.TokenType.EOF, "", null, line));
    return tokens;
  }

  private void scanToken() {
    char c = source.charAt(current);
    switch (c) {
      case '(': addToken(Token.TokenType.LEFT_PAREN); break;
      case ')': addToken(Token.TokenType.RIGHT_PAREN); break;
      case '{': addToken(Token.TokenType.LEFT_BRACE); break;
      case '}': addToken(Token.TokenType.RIGHT_BRACE); break;
      case ',': addToken(Token.TokenType.COMMA); break;
      case ';': addToken(Token.TokenType.SEMICOLON); break;
      case '+': addToken(Token.TokenType.PLUS); break;
      case '-': addToken(Token.TokenType.MINUS); break;
      case '*': addToken(Token.TokenType.STAR); break;
      case '/': addToken(Token.TokenType.SLASH); break;
      case '!': 
        if (nextCharIs('=')) {
          current++;
          addToken(Token.TokenType.NOT_EQUAL);
        } else addToken(Token.TokenType.NOT);
        break;
      case '=':
        if (nextCharIs('=')) {
          current++;
          addToken(Token.TokenType.EQUAL_EQUAL);
        } else addToken(Token.TokenType.EQUAL);
        break;
      case '<':
        if (nextCharIs('=')) {
          current++;
          addToken(Token.TokenType.LESS_EQUAL);
        } else addToken(Token.TokenType.LESS);
        break;
      case '>':
        if (nextCharIs('=')) {
          current++;
          addToken(Token.TokenType.GREATER_EQUAL);
        } else addToken(Token.TokenType.GREATER);
        break;
      case '&': // there is only &&
        if (nextCharIs('&')) {
          current++;
          addToken(Token.TokenType.AND);
        } else throw new RuntimeException("Unexpected character " + c + " at line " + line);
        break;
      case '|': // there is only ||
        if (nextCharIs('|')) {
          current++;
          addToken(Token.TokenType.OR);
        } else throw new RuntimeException("Unexpected character " + c + " at line " + line);
        break;
      case ' ': break;
      case '\r': break;
      case '\t': break;
      case '\n': line++; break;
      default:
        if (Character.isDigit(c)) { // number starts with number, followed by number
          while (Character.isDigit(peek())) current++;
          addToken(Token.TokenType.NUMBER, Integer.parseInt(source.substring(start, current + 1)));
        } else if (Character.isLetter(c) || c == '_') { // text starts with alphabet/underscore, followed by alphabet/underscore/number
          while (Character.isLetter(peek()) || peek() == '_' || Character.isDigit(peek())) current++;
          addToken(keywords.getOrDefault(source.substring(start, current + 1), Token.TokenType.IDENTIFIER));
        } else throw new RuntimeException("Unexpected character " + c + " at line " + line);
        break;
    }
  }

  private boolean nextCharIs(char expected) {
    if (isAtEnd()) return false;
    if (source.charAt(current + 1) != expected) return false;
    return true;
  }

  private char peek() {
    if (isAtEnd()) return '\0';
    return source.charAt(current + 1);
  }

  private boolean isAtEnd() { return current == source.length(); }

  private void addToken(Token.TokenType type) { addToken(type, null); }
  private void addToken(Token.TokenType type, Object literal) {
    String text = source.substring(start, current + 1);
    tokens.add(new Token(type, text, literal, line));
  }
}