package edu.wisc;
import java.util.HashMap;
import java.util.Map;

class Environment2 {
  private final Environment2 parent;
  private final Map<String, Integer> varMap = new HashMap<>();
  public int nextOffset = 0;
  public Environment2() { this.parent = null; }
  public Environment2(Environment2 parent) { this.parent = parent; }
  public Environment2(Environment2 parent, int nextOffset) {
    this.parent = parent;
    this.nextOffset = nextOffset;
  }

  public boolean containVar(Boolean searchUpmost, String name) {
    if (!searchUpmost) {
      if (varMap.containsKey(name)) return parent != null ? true : false;
      else if (parent != null) return parent.parent != null ? parent.containVar(true, name) : false;
    } else {
      if (varMap.containsKey(name)) return true;
      else if (parent != null) return parent.containVar(false, name);
    }
    return false;
  }
  public void declareVar(String name) {
    varMap.put(name, nextOffset);
    nextOffset -= 4;
  }
  public Integer getOffset(String name) {
    if (varMap.containsKey(name)) return varMap.get(name);
    else if (parent != null) return parent.getOffset(name);
    else return null;
  }
  public void updateOffset(int amount) { nextOffset += amount; }
}