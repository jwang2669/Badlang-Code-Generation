package edu.wisc; import java.util.ArrayDeque; import java.util.Deque; import java.util.List;

class Generator implements Expr.Visitor<Object>, Stmt.Visitor<Object> {
  private Deque<String> currFnName = new ArrayDeque<>();
  private Environment2 globalEnv = new Environment2();
  private Environment2 currEnv = globalEnv;
  private int ifCount = 0, whileCount = 0;
  private final StringBuilder code = new StringBuilder();
  public Generator() {}

  public String generate(List<Stmt> statements) {
    emit(".data");
    emit(".align 2");
    emit("newline: .asciiz \"\\n\"");
    for (Stmt statement : statements) { // String name, VarType type, Expr initializer / set space for global variable
      if (statement instanceof Stmt.Var) {
        emit(".align 2");
        emit(((Stmt.Var)statement).name + ": .space 4");
        globalEnv.declareVar(((Stmt.Var)statement).name);
      }
    }
    globalEnv.nextOffset = 0; // globalEnv has no offset
    emit(".text");
    emit(".globl main");
    for (Stmt statement : statements) if (statement instanceof Stmt.Function) statement.accept(this); // declare function first
    emit("main:");
    emit("move $fp, $sp"); // idk why
    for (Stmt statement : statements) if (!(statement instanceof Stmt.Function)) statement.accept(this); // main function
    emit("li $v0, 10"); // exit system call at the end of main function
    emit("syscall");
    return code.toString();
  }

////////////////////////////////////////////////////////////
  @Override public Void visitBlockStmt(Stmt.Block stmt) { // List<Stmt> statements
    Environment2 prevEnv = currEnv;
    currEnv = new Environment2(prevEnv, prevEnv.nextOffset); // stack offset persist between environment lineage
    for (Stmt statement : stmt.statements) statement.accept(this);
    currEnv = prevEnv;
    return null;
  }

  @Override public Void visitExpressionStmt(Stmt.Expression stmt) { // Expr expression
    stmt.expression.accept(this);
    return null;
  }

  @Override public Void visitFunctionStmt(Stmt.Function stmt) { // String name, VarType returnType, List<Parameter> params, List<Stmt> body / don't need .???
    currFnName.push(stmt.name);
    Environment2 prevEnv = currEnv;
    currEnv = new Environment2(globalEnv); // in function environment
    for (Stmt.Parameter param : stmt.params) currEnv.declareVar(param.name()); // parameters already on stack by function call
    emit(stmt.name + ":"); // function start
    push("$ra", "($sp)");
    push("$fp", "($sp)");
    currEnv.updateOffset(-8);
    emit("addu $fp, $sp, " + (8 + stmt.params.size() * 4)); // $fp is at start of parameters on stack
    for (Stmt statement : stmt.body) statement.accept(this); // function body
    emit(stmt.name + "_exit:"); // function exit
    emit("lw $ra, " + -(stmt.params.size() * 4) + "($fp)");
    emit("move $t0, $fp");
    emit("lw $fp, " + -(stmt.params.size() * 4 + 4) + "($fp)");
    emit("move $sp, $t0");
    emit("jr $ra");
    currEnv = prevEnv; // exit function environment
    currFnName.pop();
    return null;
  }

  @Override public Void visitIfStmt(Stmt.If stmt) { // Expr condition, Stmt thenBranch, Stmt elseBranch, doesn't require else branch
    ifCount++;
    int thisIfCount = ifCount;
    stmt.condition.accept(this);
    pop("$t0","4($sp)");
    emit("beq $t0, $zero, else_branch_" + thisIfCount); // if
    stmt.thenBranch.accept(this); // true branch
    emit("j if_else_exit_" + thisIfCount);
    emit("else_branch_" + thisIfCount + ":"); // false branch
    if (stmt.elseBranch != null) stmt.elseBranch.accept(this);
    emit("if_else_exit_" + thisIfCount + ":"); // exit
    return null;
  }

  @Override public Void visitPrintStmt(Stmt.Print stmt) { // Expr expression
    stmt.expression.accept(this);
    pop("$t0","4($sp)");
    emit("move $a0, $t0"); // print something
    emit("li $v0, 1");
    emit("syscall");
    emit("la $a0, newline"); // print a new line
    emit("li $v0, 4");
    emit("syscall");
    return null;
  }

  @Override public Void visitReturnStmt(Stmt.Return stmt) { // Expr value
    stmt.value.accept(this);
    pop("$t0","4($sp)");
    emit("sw $t0, 4($fp)");
    emit("j " + currFnName.peek() + "_exit");
    return null;
  }

  @Override public Void visitVarStmt(Stmt.Var stmt) { // String name, VarType type, Expr initializer / global variable already declared but not initialized
    if (currEnv != globalEnv) {
      emit("subu $sp, $sp, 4"); // space for new local variable
      currEnv.declareVar(stmt.name);
    }
    if (stmt.initializer == null) return null;
    stmt.initializer.accept(this);
    pop("$t0", "4($sp)");
    if (currEnv != globalEnv) emit("sw $t0, " + "4($sp)"); // local variable
    else emit("sw $t0, " + stmt.name); // global variable
    return null;
  }

  @Override public Void visitAssignStmt(Stmt.Assign stmt) { // String name, Expr value
    stmt.value.accept(this);
    pop("$t0", "4($sp)");
    if (currEnv.containVar(false, stmt.name)) emit("sw $t0, " + currEnv.getOffset(stmt.name) + "($fp)"); // local variable
    else emit("sw $t0, " + stmt.name); // global variable
    return null;
  }

  @Override public Void visitWhileStmt(Stmt.While stmt) { // Expr condition, Stmt body
    whileCount++;
    int thisWhileCount = whileCount;
    emit("while_loop_" + thisWhileCount + ":"); // while loop head
    stmt.condition.accept(this);
    pop("$t0", "4($sp)");
    emit("beq $t0, $zero, while_loop_exit_" + thisWhileCount); // evaluate condition
    stmt.body.accept(this); // while loop body
    emit("j while_loop_" + thisWhileCount); // loop back to head
    emit("while_loop_exit_" + thisWhileCount + ":"); // exit
    return null;
  }

//// always push expression result on stack //////////////////
//////////////////////////////////////////////////////////////
  @Override public Void visitBinaryExpr(Expr.Binary expr) { // Expr left, Operator operator, Expr right
    expr.left.accept(this);
    expr.right.accept(this);
    pop("$t1", "4($sp)");
    pop("$t0", "4($sp)");
    switch (expr.operator) {
      case          PLUS: emit("add $t0, $t0, $t1"); break;
      case         MINUS: emit("sub $t0, $t0, $t1"); break;
      case      MULTIPLY: emit("mult $t0, $t1"); emit("mflo $t0"); break;
      case        DIVIDE: emit("div $t0, $t1"); emit("mflo $t0"); break;
      case           AND: emit("and $t0, $t0, $t1"); break;
      case            OR: emit("or $t0, $t0, $t1"); break;
      case         EQUAL: emit("sub $t0, $t0, $t1"); emit("sltiu $t0, $t0, 1"); break;
      case     NOT_EQUAL: emit("sub $t0, $t0, $t1"); emit("sltu $t0, $zero, $t0"); break;
      case          LESS: emit("slt $t0, $t0, $t1"); break;
      case    LESS_EQUAL: emit("slt $t0, $t1, $t0"); emit("xori $t0, $t0, 1"); break;
      case       GREATER: emit("slt $t0, $t1, $t0"); break;
      case GREATER_EQUAL: emit("slt $t0, $t0, $t1"); emit("xori $t0, $t0, 1"); break;
    }
    push("$t0", "($sp)");
    return null;
  }

  @Override public Void visitLiteralExpr(Expr.Literal expr) { // Object value
    if (expr.value instanceof Boolean) emit("li $t0, " + ((Boolean)expr.value ? 1 : 0)); // convert boolean to 0 1
    else if (expr.value instanceof Integer) emit("li $t0, " + expr.value); // int
    push("$t0", "($sp)");
    return null;
  }

  @Override public Void visitUnaryExpr(Expr.Unary expr) { // Operator operator, Expr right
    expr.right.accept(this);
    pop("$t0", "4($sp)");
    if (expr.operator == Operator.MINUS) { // !
      emit("sub $t1, $zero, $t0");
      push("$t1", "($sp)");
    } else if (expr.operator == Operator.NOT) { // -
      emit("xori $t1, $t0, 1");
      push("$t1", "($sp)");
    }
    return null;
  }

  @Override public Void visitVariableExpr(Expr.Variable expr) { // String name
    if (currEnv.containVar(false, expr.name)) { // local variable
      emit("lw $t0, " + currEnv.getOffset(expr.name) + "($fp)");
      push("$t0", "($sp)");
    } else { // global variable
      emit("lw $t0, " + expr.name);
      push("$t0", "($sp)");
    }
    return null;
  }

  @Override public Void visitCallExpr(Expr.Call expr) { // String name, List<Expr> arguments
    emit("subu $sp, $sp, 4"); // space for return value
    for (Expr argument : expr.arguments) argument.accept(this);
    emit("jal " + expr.name); // jump to function label
    return null; // return value is right below $sp on the stack now
  }

////////////////////////////////////////////////////////
  private void emit(String s) { code.append(s + "\n"); }
  private void pop(String destination, String source) {
    code.append("lw " + destination + ", " + source + "\n");
    code.append("addu $sp, $sp, 4\n");
  }
  private void push(String source, String destination) {
    code.append("sw " + source + ", " + destination + "\n");
    code.append("subu $sp, $sp, 4\n");
  }
}