package jess;

class Node1MTEQ extends Node1 {
  Node1MTEQ(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Value paramValue, Rete paramRete) {
    super(20, paramInt1, paramInt2, paramInt3, paramInt4, paramValue, paramRete);
  }

  boolean callNode(Token paramToken, int paramInt) throws ReteException {
    if (super.callNode(paramToken, paramInt))
      return false;
    boolean bool = false;
    ValueVector valueVector = paramToken.fact(0);
    Value value;
    if (valueVector.size() >= this.R3 && (value = valueVector.get(this.R3)).type() == 512) {
      ValueVector valueVector1 = value.listValue();
      if (valueVector1.size() >= this.R4) {
        Value value1 = valueVector1.get(this.R4);
        if (this.m_value.type() == 64) {
          int i = this.m_cache.markFuncall();
          int j = this.m_cache.markValue();
          if (!Funcall.execute(eval(this.m_value, paramToken), this.m_engine.globalContext(), this.m_cache).equals(Funcall.FALSE()))
            bool = true;
          this.m_cache.restoreFuncall(i);
          this.m_cache.restoreValue(j);
        } else if (value1.equals(this.m_value)) {
          bool = true;
        }
      }
    }
    if (bool)
      passAlong(paramToken);
    return bool;
  }
}
