package jess;

class Node1TELN extends Node1 {
  Node1TELN(
      int paramInt1,
      int paramInt2,
      int paramInt3,
      int paramInt4,
      Value paramValue,
      Rete paramRete) {
    super(6, paramInt1, paramInt2, paramInt3, paramInt4, paramValue, paramRete);
  }

  boolean callNode(Token paramToken, int paramInt) throws ReteException {
    if (super.callNode(paramToken, paramInt)) return false;
    ValueVector valueVector = paramToken.fact(0);
    boolean bool = (valueVector.size() == this.R1) ? true : false;
    if (bool) passAlong(paramToken);
    return bool;
  }
}
