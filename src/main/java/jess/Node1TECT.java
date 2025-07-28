package jess;

class Node1TECT extends Node1 {
  Node1TECT(
      int paramInt1,
      int paramInt2,
      int paramInt3,
      int paramInt4,
      Value paramValue,
      Rete paramRete) {
    super(2, paramInt1, paramInt2, paramInt3, paramInt4, paramValue, paramRete);
  }

  boolean callNode(Token paramToken, int paramInt) throws ReteException {
    if (super.callNode(paramToken, paramInt)) return false;
    ValueVector valueVector = paramToken.fact(0);
    boolean bool =
        (valueVector.get(0).atomValue() == this.R1
                && valueVector.get(1).descriptorValue() == this.R2)
            ? true
            : false;
    if (bool) passAlong(paramToken);
    return bool;
  }
}
