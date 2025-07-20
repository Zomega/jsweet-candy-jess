package jess;

class Node1MTELN extends Node1 {
  Node1MTELN(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Value paramValue, Rete paramRete) {
    super(22, paramInt1, paramInt2, paramInt3, paramInt4, paramValue, paramRete);
  }

  boolean callNode(Token paramToken, int paramInt) throws ReteException {
    if (super.callNode(paramToken, paramInt))
      return false;
    ValueVector valueVector = paramToken.fact(0);
    boolean bool = false;
    Value value;
    if (valueVector.size() >= this.R1 && (value = valueVector.get(this.R1)).type() == 512) {
      ValueVector valueVector1 = value.listValue();
      if (valueVector1.size() == this.R2)
        bool = true;
    }
    if (bool)
      passAlong(paramToken);
    return bool;
  }
}
