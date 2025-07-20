package jess;

class Node1TNEV1 extends Node1 {
  Node1TNEV1(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Value paramValue, Rete paramRete) {
    super(4, paramInt1, paramInt2, paramInt3, paramInt4, paramValue, paramRete);
  }

  boolean callNode(Token paramToken, int paramInt) throws ReteException {
    if (super.callNode(paramToken, paramInt))
      return false;
    boolean bool = false;
    ValueVector valueVector = paramToken.fact(0);
    if (valueVector.size() >= this.R1 && valueVector.size() >= this.R2) {
      Value value1;
      Value value2;
      if (this.R3 != -1) {
        value1 = valueVector.get(this.R1).listValue().get(this.R3);
      } else {
        value1 = valueVector.get(this.R1);
      }
      if (this.R4 != -1) {
        value2 = valueVector.get(this.R2).listValue().get(this.R4);
      } else {
        value2 = valueVector.get(this.R2);
      }
      bool = !value1.equals(value2) ? true : false;
    }
    if (bool)
      passAlong(paramToken);
    return bool;
  }
}
