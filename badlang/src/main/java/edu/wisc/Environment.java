package edu.wisc;
import java.util.HashMap;
import java.util.Map;

class Environment {
  private final Environment parent;
  private final Map<String, VarType> varMap = new HashMap<>();
  private final Map<String, Stmt.Function> fnMap = new HashMap<>();
  public Environment() { this.parent = null; }
  public Environment(Environment parent) { this.parent = parent; }
  
  boolean containVar(Boolean searchUpward, String name) {
    if (varMap.containsKey(name)) return true;
    else if (searchUpward == true && parent != null) return parent.containVar(true, name);
    else return false;
  }
  void declareVar(String name, VarType type) { varMap.put(name, type); }
  void assignVar(String name, VarType type) {
    if (varMap.containsKey(name)) varMap.put(name, type);
    else if (parent != null) parent.assignVar(name, type);
  }
  VarType getVar(String name) {
    if (varMap.containsKey(name)) return varMap.get(name);
    else if (parent != null) return parent.getVar(name);
    return null;
  }

  boolean containFn(String name) {
    if (fnMap.containsKey(name)) return true;
    else if (parent != null) return parent.containFn(name);
    else return false;
  }
  void declareFn(String name, Stmt.Function fn) { fnMap.put(name, fn); }
  Stmt.Function getFn(String name) {
    if (fnMap.containsKey(name)) return fnMap.get(name);
    else if (parent != null) return parent.getFn(name);
    return null;
  }
}