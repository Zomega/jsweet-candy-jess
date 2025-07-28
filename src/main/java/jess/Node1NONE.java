package jess;

class Node1NONE extends Node1 {
  Node1NONE(
      int paramInt1,
      int paramInt2,
      int paramInt3,
      int paramInt4,
      Value paramValue,
      Rete paramRete) {
    super(30, paramInt1, paramInt2, paramInt3, paramInt4, paramValue, paramRete);
  }

  boolean callNode(Token paramToken, int paramInt) throws ReteException {
    super.callNode(paramToken, paramInt);
    return false;
  }
}
