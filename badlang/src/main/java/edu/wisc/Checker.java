package edu.wisc; import java.util.List;

class Checker implements Expr.Visitor<VarType>, Stmt.Visitor<Object> {
  private Environment globals = new Environment();
  private Environment environment = globals;
  private VarType currentFnReturnType = null;
  int line = 1; // assume line is always at correct line number at function start, should update line to correct line number at function end
  public Checker() {}

  public void check(List<Stmt> statements) { // call this function to start the whole thing
    for (Stmt statement : statements) { // declare all functions first so function can be accessed before declaration
      if (statement instanceof Stmt.Function fn) {
        if (environment.containFn(fn.name)) error("Name", "Function " + fn.name + " already declared"); // function can't be declared twice
        environment.declareFn(fn.name, fn);
      }
    }
    for (Stmt statement : statements) {
      statement.accept(this);
      line++;
    }
  }
  public void error(String type, String message) { throw new RuntimeException("Line " + line + ": " + type + " error: " + message); }

  @Override public Void visitBlockStmt(Stmt.Block stmt) { // List<Stmt> statements
    Environment previous = environment;
    environment = new Environment(environment);
    for (Stmt statement : stmt.statements) {
      line++;
      statement.accept(this);
    }
    line++;
    environment = previous;
    return null;
  }

  @Override public Void visitExpressionStmt(Stmt.Expression stmt) { // Expr expression
    stmt.expression.accept(this);
    return null;
  }

  @Override public Void visitFunctionStmt(Stmt.Function stmt) { // String name, VarType returnType, List<Parameter> params, List<Stmt> body
    Environment previous = environment; // function declaration moved to check()
    environment = new Environment(environment);
    for (Stmt.Parameter param : stmt.params) environment.declareVar(param.name(), param.type()); // declare function parameters in its environment
    currentFnReturnType = stmt.returnType;
    for (Stmt bodyStmt : stmt.body) {
      line++;
      bodyStmt.accept(this);
    }
    line++;
    environment = previous;
    return null;
  }

  @Override public Void visitIfStmt(Stmt.If stmt) { // Expr condition, Stmt thenBranch, Stmt elseBranch, doesn't require else branch
    VarType condType = stmt.condition.accept(this);
    if (condType != VarType.BOOL) error("Type", "Condition in if expects bool, got" + condType); // condition has to be bool
    stmt.thenBranch.accept(this);
    if (stmt.elseBranch != null) stmt.elseBranch.accept(this);
    return null;
  }

  @Override public Void visitPrintStmt(Stmt.Print stmt) { // Expr expression
    VarType type = stmt.expression.accept(this);
    if (type != VarType.BOOL && type != VarType.INT) error("Type", "Print statement expects bool/int, got " + type); // only print bool/int
    return null;
  }

  @Override public Void visitReturnStmt(Stmt.Return stmt) { // Expr value
    VarType valueType = stmt.value.accept(this);
    if (valueType != currentFnReturnType) error("Type", "Return statement expects " + currentFnReturnType + ", got " + valueType); // match function return type
    return null;
  }

  @Override public Void visitVarStmt(Stmt.Var stmt) { // String name, VarType type, Expr initializer
    if (environment.containVar(false, stmt.name)) error("Name", "Variable " + stmt.name + " already declared in this scope"); // can't declare same variable twice in same scope
    environment.declareVar(stmt.name, stmt.type);
    if (stmt.initializer != null) {
      VarType initType = stmt.initializer.accept(this);
      if (initType != stmt.type) error("Name", "Variable " + stmt.name + " expects " + stmt.type + ", got " + initType); // initializer type needs to match variable type
    }
    return null;
  }

  @Override public Void visitAssignStmt(Stmt.Assign stmt) { // String name, Expr value
    if (!environment.containVar(true, stmt.name)) error("Name", "Undefined variable " + stmt.name); // has to declare variable first before assigning value to it
    VarType varType = environment.getVar(stmt.name), valueType = stmt.value.accept(this);
    if (valueType != varType) error("Type", "Variable " + stmt.name + " expects " + varType + ", got " + valueType); // value type needs to match variable type
    return null;
  }

  @Override public Void visitWhileStmt(Stmt.While stmt) { // Expr condition, Stmt body
    VarType condType = stmt.condition.accept(this);
    if (condType != VarType.BOOL) error("Type", "Condition in while expects bool, got " + condType); // condition has to be bool
    stmt.body.accept(this);
    return null;
  }

  ///////////////////////////////////////////////////////////////
  @Override public VarType visitBinaryExpr(Expr.Binary expr) { // Expr left, Operator operator, Expr right
    VarType leftType = expr.left.accept(this), rightType = expr.right.accept(this);
    switch (expr.operator) {
      case PLUS: case MINUS: case MULTIPLY: case DIVIDE:
        if (leftType != VarType.INT || rightType != VarType.INT) error("Type", "Arithmetic operators require int");
        return VarType.INT;
      case LESS: case LESS_EQUAL: case GREATER: case GREATER_EQUAL:
        if (leftType != VarType.INT || rightType != VarType.INT) error("Type", "Comparison operators require int");
        return VarType.BOOL;
      case EQUAL: case NOT_EQUAL:
        if (leftType != rightType) error("Type", "Equality operators require matching types of either bool or int");
        return VarType.BOOL;
      case AND: case OR:
        if (leftType != VarType.BOOL || rightType != VarType.BOOL) error("Type", "Logical operators require bool");
        return VarType.BOOL;
      default: error("Type", "Unknown binary operator " + expr.operator); return null;
    }
  }

  @Override public VarType visitLiteralExpr(Expr.Literal expr) { // Object value
    if (expr.value instanceof Boolean) return VarType.BOOL;
    if (expr.value instanceof Integer) return VarType.INT;
    error("Type", "Literal expects bool/int, got " + expr.value); return null;
  }

  @Override public VarType visitUnaryExpr(Expr.Unary expr) { // Operator operator, Expr right
    VarType rightType = expr.right.accept(this);
    switch (expr.operator) {
      case MINUS:
        if (rightType != VarType.INT) error("Type", "Unary - requires int");
        return VarType.INT;
      case NOT:
        if (rightType != VarType.BOOL) error("Type", "Operator ! requires bool");
        return VarType.BOOL;
      default: error("Type", "Unary operator expects -/!, got " + expr.operator); return null;
    }
  }

  @Override public VarType visitVariableExpr(Expr.Variable expr) { // String name
    if (!environment.containVar(true, expr.name)) error("Name", "Undefined variable " + expr.name);
    return environment.getVar(expr.name);
  }

  @Override public VarType visitCallExpr(Expr.Call expr) { // String name, List<Expr> arguments
    if (!environment.containFn(expr.name)) error("Name", "Undefined function " + expr.name);
    Stmt.Function fn = environment.getFn(expr.name); // String name, VarType returnType, List<Parameter> params, List<Stmt> body
    if (expr.arguments.size() != fn.params.size()) error("Type", "Function " + expr.name + " expects " + fn.params.size() + " arguments, got " + expr.arguments.size());
    for (int i = 0; i < fn.params.size(); i++) { // check if arguments match function parameters
      VarType argType = expr.arguments.get(i).accept(this), paramType = fn.params.get(i).type();
      if (argType != paramType) error("Type", "Argument " + (i + 1) + " of " + expr.name + " expects " + paramType + ", got " + argType);
    }
    return fn.returnType;
  }
}