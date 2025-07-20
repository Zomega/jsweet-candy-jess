package jess;

import java.util.Vector;

public abstract class Node {
  static final int LEFT = 0;

  static final int RIGHT = 1;

  static final int SINGLE = 2;

  static final int ACTIVATE = 3;

  int m_command;

  int m_usecount = 1;

  Vector m_succ = new Vector();

  Successor[] m_localSucc;

  int m_nsucc;

  Rete m_engine;

  EvalCache m_cache;

  public final Vector succ() {
    return this.m_succ;
  }

  Node(Rete paramRete) {
    this.m_engine = paramRete;
    this.m_cache = new EvalCache();
  }

  Funcall eval(Value paramValue, Token paramToken) throws ReteException {
    Funcall funcall = paramValue.funcallValue().cloneInto(this.m_cache.getFuncall());
    for (byte b = 0; b < funcall.size(); b++) {
      if (funcall.get(b).type() == 4096) {
        int[] arrayOfInt = funcall.get(b).intArrayValue();
        if (arrayOfInt[2] == -1) {
          funcall.set(paramToken.fact(arrayOfInt[0]).get(arrayOfInt[1]), b);
        } else {
          ValueVector valueVector = paramToken.fact(arrayOfInt[0]).get(arrayOfInt[1]).listValue();
          funcall.set(valueVector.get(arrayOfInt[2]), b);
        }
      } else if (funcall.get(b).type() == 64) {
        Value value = this.m_cache.getValue();
        value.resetValue(eval(funcall.get(b), paramToken), 64);
        funcall.set(value, b);
      }
    }
    return funcall;
  }

  void freeze() {
    this.m_nsucc = this.m_succ.size();
    if (this.m_localSucc == null || this.m_localSucc.length < this.m_nsucc)
      this.m_localSucc = new Successor[this.m_nsucc];
    for (byte b = 0; b < this.m_nsucc; b++) {
      this.m_localSucc[b] = this.m_succ.elementAt(b);
      (this.m_localSucc[b]).m_node.freeze();
    }
  }

  void removeSuccessor(Successor paramSuccessor) {
    this.m_succ.removeElement(paramSuccessor);
  }

  abstract boolean callNode(Token paramToken, int paramInt) throws ReteException;

  final void passAlong(Token paramToken) throws ReteException {
    Successor[] arrayOfSuccessor = this.m_localSucc;
    for (byte b = 0; b < this.m_nsucc; b++) {
      Successor successor = arrayOfSuccessor[b];
      successor.m_node.callNode(paramToken, successor.m_callType);
    }
  }
}
