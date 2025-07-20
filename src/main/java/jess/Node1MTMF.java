package jess;

class Node1MTMF extends Node1 {
  Node1MTMF(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Value paramValue, Rete paramRete) {
    super(23, paramInt1, paramInt2, paramInt3, paramInt4, paramValue, paramRete);
  }

  boolean callNode(Token paramToken, int paramInt) throws ReteException {
    if (super.callNode(paramToken, paramInt))
      return false;
    ValueVector valueVector1 = paramToken.fact(0);
    int i = this.R1;
    int j = this.R2;
    if (valueVector1.get(i).equals(Funcall.NIL()))
      return false;
    ValueVector valueVector2 = valueVector1.get(i).listValue();
    int k = valueVector2.size();
    for (byte b = 0; b < k - j + 1; b++) {
      int n = j + 1 + b;
      int i1 = k - n + 1;
      ValueVector valueVector3 = new ValueVector(n);
      int m;
      for (m = 0; m < j; m++)
        valueVector3.add(valueVector2.get(m));
      ValueVector valueVector4 = new ValueVector(i1);
      valueVector3.add(new Value(valueVector4, 512));
      for (m = j; m < i1 + j; m++)
        valueVector4.add(valueVector2.get(m));
      for (m = j + i1; m < n - 1 + i1; m++)
        valueVector3.add(valueVector2.get(m));
      ValueVector valueVector5 = (ValueVector)valueVector1.clone();
      valueVector5.set(new Value(valueVector3, 512), i);
      Token token = new Token(paramToken.m_tag, valueVector5);
      passAlong(token);
    }
    return true;
  }
}
