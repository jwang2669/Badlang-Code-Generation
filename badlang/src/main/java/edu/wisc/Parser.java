package edu.wisc;
import java.util.*;

/* The Parser converts a sequence of tokens into an Abstract Syntax Tree (AST).
 * 1. Take a list of tokens from the lexer
 * 2. Build an AST using the provided Expr and Stmt classes
 * 3. Implement recursive descent parsing based on the CFG you define
 * 4. Handle operator precedence correctly
 * 5. Report syntax errors with meaningful messages */
public class Parser {
  private final List<Token> tokens;
  private int current = 0;
  public Parser(List<Token> tokens) { this.tokens = tokens; }

  public List<Stmt> parseProgram() { // program -> declaration* EOF
    List<Stmt> statements = new ArrayList<>();
    while (!tokenTypeMatches(Token.TokenType.EOF)) {
      statements.add(declaration());
      current++;
    }
    return statements;
  }

  // assume current is at first character when entering a function, and at last character when leaving a function
  private Stmt declaration() { // declaration -> varDecl | funDecl | statement
    if (tokenTypeMatches(Token.TokenType.INT, Token.TokenType.BOOL)) return varDeclaration();
    if (tokenTypeMatches(Token.TokenType.FUN)) return functionDeclaration();
    return statement();
  }

  private Stmt varDeclaration() { // varDecl -> type IDENTIFIER ("=" expression)? ";"
    VarType type; String name; Expr initializer;
    type = parseTokenType(tokens.get(current));
    current++; name = requireTokenOfType("Expect variable name.", Token.TokenType.IDENTIFIER).getLexeme();
    if (nextTokenTypeMatches(Token.TokenType.EQUAL)) { current += 2; initializer = expression(); } // check ("=" expression)?
    else initializer = null;
    current++; requireTokenOfType("Expect ; after variable declaration.", Token.TokenType.SEMICOLON);
    return new Stmt.Var(name, type, initializer);
  }

  private Stmt functionDeclaration() { // funDecl -> "fun" type IDENTIFIER "(" parameters? ")" block
    VarType returnType; String name; List<Stmt.Parameter> params = new ArrayList<>(); List<Stmt> body;
    current++; returnType = parseTokenType(requireTokenOfType("Expect int/bool return type.", Token.TokenType.INT, Token.TokenType.BOOL));
    current++; name = requireTokenOfType("Expect function name.", Token.TokenType.IDENTIFIER).getLexeme();
    current++; requireTokenOfType("Expect ( after function name.", Token.TokenType.LEFT_PAREN);
    if (!nextTokenTypeMatches(Token.TokenType.RIGHT_PAREN)) {
      do { // check parameters?
        current++; VarType paramType = parseTokenType(requireTokenOfType("Expect parameter type.", Token.TokenType.INT, Token.TokenType.BOOL));
        current++; String paramName = requireTokenOfType("Expect parameter name.", Token.TokenType.IDENTIFIER).getLexeme();
        params.add(new Stmt.Parameter(paramName, paramType));
        current++;
      } while (tokenTypeMatches(Token.TokenType.COMMA));
    } else current++;
    requireTokenOfType("Expect ) after parameters.", Token.TokenType.RIGHT_PAREN);
    current++; body = block().statements;
    return new Stmt.Function(name, returnType, params, body);
  }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  private Stmt statement() { // statement -> exprStmt | printStmt | block | ifStmt | whileStmt | returnStmt | assignStmt
    if (tokenTypeMatches(Token.TokenType.PRINT)) return printStatement();
    if (tokenTypeMatches(Token.TokenType.LEFT_BRACE)) return block();
    if (tokenTypeMatches(Token.TokenType.IF)) return ifStatement();
    if (tokenTypeMatches(Token.TokenType.WHILE)) return whileStatement();
    if (tokenTypeMatches(Token.TokenType.RETURN)) return returnStatement();
    if (tokenTypeMatches(Token.TokenType.IDENTIFIER) && nextTokenTypeMatches(Token.TokenType.EQUAL)) return assignment();
    return expressionStatement();
  }

  private Stmt printStatement() { // printStmt -> "print" expression ";"
    current++; Expr expression = expression();
    current++; requireTokenOfType("Expect ; after expression.", Token.TokenType.SEMICOLON);
    return new Stmt.Print(expression);
  }

  private Stmt.Block block() { // block -> "{" declaration* "}"
    List<Stmt> statements = new ArrayList<>();
    while (!nextTokenTypeMatches(Token.TokenType.RIGHT_BRACE)) { // check declaration*
      current++;
      statements.add(declaration());
    }
    current++; requireTokenOfType("Expect } after block.", Token.TokenType.RIGHT_BRACE);
    return new Stmt.Block(statements);
  }

  private Stmt ifStatement() { // ifStmt -> "if" "(" expression ")" block ("else" block)?
    Expr condition; Stmt thenBranch, elseBranch = null;
    current++; requireTokenOfType("Expect ( after if.", Token.TokenType.LEFT_PAREN);
    current++; condition = expression();
    current++; requireTokenOfType("Expect ) after condition.", Token.TokenType.RIGHT_PAREN);
    current++; thenBranch = block();
    if (nextTokenTypeMatches(Token.TokenType.ELSE)) { // check ("else" block)?
      current += 2;
      elseBranch = block();
    }
    return new Stmt.If(condition, thenBranch, elseBranch);
  }

  private Stmt whileStatement() { // whileStmt -> "while" "(" expression ")" block
    current++; requireTokenOfType("Expect ( after while.", Token.TokenType.LEFT_PAREN);
    current++; Expr condition = expression();
    current++; requireTokenOfType("Expect ) after condition.", Token.TokenType.RIGHT_PAREN);
    current++; Stmt body = block();
    return new Stmt.While(condition, body);
  }

  private Stmt returnStatement() { // returnStmt -> "return" expression ";"
    current++; Expr value = expression();
    current++; requireTokenOfType("Expect ; after return value.", Token.TokenType.SEMICOLON);
    return new Stmt.Return(value);
  }

  private Stmt assignment() { // assignStmt -> IDENTIFIER "=" expression ";"
    String name = requireTokenOfType("Expect variable name.", Token.TokenType.IDENTIFIER).getLexeme();
    current++; requireTokenOfType("Expect = in assignment.", Token.TokenType.EQUAL);
    current++; Expr value = expression();
    current++; requireTokenOfType("Expect ; after assignment.", Token.TokenType.SEMICOLON);
    return new Stmt.Assign(name, value);
  }

  private Stmt expressionStatement() { // exprStmt -> expression ";"
    Expr expr = expression();
    current++; requireTokenOfType("Expect ; after expression.", Token.TokenType.SEMICOLON);
    return new Stmt.Expression(expr);
  }

///////////////////////////////////////////////////////////////////////////
  private Expr expression() { return logicOr(); } // expression -> logic_or

  private Expr logicOr() { // logic_or -> logic_and ("||" logic_and)*
    Expr expr = logicAnd();
    while (nextTokenTypeMatches(Token.TokenType.OR)) { // check ("||" logic_and)*
      current += 2; Expr right = logicAnd();
      expr = new Expr.Binary(expr, Operator.OR, right);
    }
    return expr;
  }

  private Expr logicAnd() { // logic_and -> equality ("&&" equality)*
    Expr expr = equality();
    while (nextTokenTypeMatches(Token.TokenType.AND)) { // check ("&&" equality)*
      current += 2; Expr right = equality();
      expr = new Expr.Binary(expr, Operator.AND, right);
    }
    return expr;
  }

  private Expr equality() { // equality -> comparison (("==" | "!=") comparison)*
    Expr expr = comparison();
    while (nextTokenTypeMatches(Token.TokenType.EQUAL_EQUAL, Token.TokenType.NOT_EQUAL)) { // check (("==" | "!=") comparison)*
      current++; Operator operator = parseTokenOperator(tokens.get(current));
      current++; Expr right = comparison();
      expr = new Expr.Binary(expr, operator, right);
    }
    return expr;
  }

  private Expr comparison() { // comparison -> term (("<" | "<=" | ">" | ">=") term)*
    Expr expr = term();
    while (nextTokenTypeMatches(Token.TokenType.GREATER, Token.TokenType.GREATER_EQUAL, Token.TokenType.LESS, Token.TokenType.LESS_EQUAL)) { // check (("<" | "<=" | ">" | ">=") term)*
      current++; Operator operator = parseTokenOperator(tokens.get(current));
      current++; Expr right = term();
      expr = new Expr.Binary(expr, operator, right);
    }
    return expr;
  }

  private Expr term() { // term -> factor (("+" | "-") factor)*
    Expr expr = factor();
    while (nextTokenTypeMatches(Token.TokenType.MINUS, Token.TokenType.PLUS)) { // check (("+" | "-") factor)*
      current++; Operator operator = parseTokenOperator(tokens.get(current));
      current++; Expr right = factor();
      expr = new Expr.Binary(expr, operator, right);
    }
    return expr;
  }

  private Expr factor() { // factor -> unary (("*" | "/") unary)*
    Expr expr = unary();
    while (nextTokenTypeMatches(Token.TokenType.SLASH, Token.TokenType.STAR)) { // check (("*" | "/") unary)*
      current++; Operator operator = parseTokenOperator(tokens.get(current));
      current++; Expr right = unary();
      expr = new Expr.Binary(expr, operator, right);
    }
    return expr;
  }

  private Expr unary() { // unary -> ("!" | "-") unary | call
    if (tokenTypeMatches(Token.TokenType.NOT, Token.TokenType.MINUS)) {
      Operator operator = parseTokenOperator(tokens.get(current));
      current++; Expr right = unary();
      return new Expr.Unary(operator, right);
    }
    return call();
  }

  private Expr call() { // call -> primary "(" arguments? ")" | primary
    Expr expr = primary();
    if (nextTokenTypeMatches(Token.TokenType.LEFT_PAREN)) { // call is actually a call in this case, not a primary
      List<Expr> arguments = new ArrayList<>();
      current++;
      if (!nextTokenTypeMatches(Token.TokenType.RIGHT_PAREN)) {
        do {
          current++; arguments.add(expression());
          current++;
        } while (tokenTypeMatches(Token.TokenType.COMMA));
      } else current++;
      requireTokenOfType("Expect ) after arguments.", Token.TokenType.RIGHT_PAREN);
      if (expr instanceof Expr.Variable) return new Expr.Call(((Expr.Variable)expr).name, arguments);
      else throw new RuntimeException("Can only call functions by name. At " + tokens.get(current));
    }
    return expr;
  }

  private Expr primary() { // primary -> "true" | "false" | INTEGER | IDENTIFIER | "(" expression ")"
    if (tokenTypeMatches(Token.TokenType.TRUE)) return new Expr.Literal(true);
    if (tokenTypeMatches(Token.TokenType.FALSE)) return new Expr.Literal(false);
    if (tokenTypeMatches(Token.TokenType.NUMBER)) return new Expr.Literal(Integer.parseInt(tokens.get(current).getLexeme()));
    if (tokenTypeMatches(Token.TokenType.IDENTIFIER)) return new Expr.Variable(tokens.get(current).getLexeme());
    if (tokenTypeMatches(Token.TokenType.LEFT_PAREN)) {
      current++; Expr expr = expression();
      current++; requireTokenOfType("Expect ) after expression.", Token.TokenType.RIGHT_PAREN);
      return expr;
    }
    throw new RuntimeException("Expect expression. At " + tokens.get(current));
  }

/////////////////////////////////////////////////////////////////////////////////////
  private Operator parseTokenOperator(Token token) { // change Token Type to Operator
    switch (token.getType()) {
      case PLUS: return Operator.PLUS;
      case MINUS: return Operator.MINUS;
      case STAR: return Operator.MULTIPLY;
      case SLASH: return Operator.DIVIDE;
      case EQUAL_EQUAL: return Operator.EQUAL;
      case NOT_EQUAL: return Operator.NOT_EQUAL;
      case GREATER: return Operator.GREATER;
      case GREATER_EQUAL: return Operator.GREATER_EQUAL;
      case LESS: return Operator.LESS;
      case LESS_EQUAL: return Operator.LESS_EQUAL;
      case NOT: return Operator.NOT;
      default: throw new RuntimeException(token.getType() + " isn't part of Operator class at " + token);
    }
  }
  private VarType parseTokenType(Token token) { // change Token Type to VarType
    switch (token.getType()) {
      case INT: return VarType.INT;
      case BOOL: return VarType.BOOL;
      default: throw new RuntimeException("There is only INT and BOOL in VarType at " + token);
    }
  }

  private Token requireTokenOfType(String error, Token.TokenType... types) { // check and return current token if type matches, otherwise error
    if (tokenTypeMatches(types)) return tokens.get(current);
    throw new RuntimeException(error + " At " + tokens.get(current));
  }
  private boolean tokenTypeMatches(Token.TokenType... types) {
    if (current >= tokens.size()) return false;
    for (Token.TokenType type : types) if (tokens.get(current).getType() == type) return true;
    return false;
  }
  private boolean nextTokenTypeMatches(Token.TokenType... types) {
    if (current >= tokens.size() - 1) return false;
    for (Token.TokenType type : types) if (tokens.get(current + 1).getType() == type) return true;
    return false;
  }
}