package jess;

class Node1TMF extends Node1 {
  Node1TMF(
      int paramInt1,
      int paramInt2,
      int paramInt3,
      int paramInt4,
      Value paramValue,
      Rete paramRete) {
    super(7, paramInt1, paramInt2, paramInt3, paramInt4, paramValue, paramRete);
  }

  boolean callNode(Token paramToken, int paramInt) throws ReteException {
    if (super.callNode(paramToken, paramInt)) return false;
    ValueVector valueVector = paramToken.fact(0);
    int i = valueVector.size();
    for (byte b = 0; b < i - this.R1 + 1; b++) {
      int k = this.R1 + 1 + b;
      int m = i - k + 1;
      ValueVector valueVector1 = new ValueVector(k);
      int j;
      for (j = 0; j < this.R1; j++) valueVector1.add(valueVector.get(j));
      ValueVector valueVector2 = new ValueVector(m);
      valueVector1.add(new Value(valueVector2, 512));
      for (j = this.R1; j < m + this.R1; j++) valueVector2.add(valueVector.get(j));
      for (j = this.R1 + m; j < k - 1 + m; j++) valueVector1.add(valueVector.get(j));
      Token token = new Token(paramToken.m_tag, valueVector1);
      passAlong(token);
    }
    return true;
  }
}
