package edu.wisc;
import java.util.List;

abstract class Expr {
  interface Visitor<R> {
    R visitBinaryExpr(Binary expr);
    R visitLiteralExpr(Literal expr);
    R visitUnaryExpr(Unary expr);
    R visitVariableExpr(Variable expr);
    R visitCallExpr(Call expr);
  }

  static class Binary extends Expr {
    final Expr left; final Operator operator; final Expr right;
    Binary(Expr left, Operator operator, Expr right) { this.left = left; this.operator = operator; this.right = right; }
    @Override <R> R accept(Visitor<R> visitor) { return visitor.visitBinaryExpr(this); }
  }

  static class Literal extends Expr {
    final Object value;
    Literal(Object value) { this.value = value; }
    @Override <R> R accept(Visitor<R> visitor) { return visitor.visitLiteralExpr(this); }
  }

  static class Unary extends Expr {
    final Operator operator; final Expr right;
    Unary(Operator operator, Expr right) { this.operator = operator; this.right = right; }
    @Override <R> R accept(Visitor<R> visitor) { return visitor.visitUnaryExpr(this); }
  }

  static class Variable extends Expr {
    final String name;
    Variable(String name) { this.name = name; }
    @Override <R> R accept(Visitor<R> visitor) { return visitor.visitVariableExpr(this); }
  }

  static class Call extends Expr {
    final String name; final List<Expr> arguments;
    Call(String name, List<Expr> arguments) { this.name = name; this.arguments = arguments; }
    @Override <R> R accept(Visitor<R> visitor) { return visitor.visitCallExpr(this); }
  }

  abstract <R> R accept(Visitor<R> visitor);
}