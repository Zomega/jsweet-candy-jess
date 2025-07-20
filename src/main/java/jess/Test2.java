package jess;

class Test2 {
  static final int EQ = 0;

  static final int NEQ = 1;

  int m_test;

  int m_tokenIdx;

  int m_leftIdx;

  int m_leftSubIdx;

  int m_rightIdx;

  int m_rightSubIdx;

  Test2(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6) {
    this.m_test = paramInt1;
    this.m_tokenIdx = paramInt2;
    this.m_rightIdx = paramInt5;
    this.m_rightSubIdx = paramInt6;
    this.m_leftIdx = paramInt3;
    this.m_leftSubIdx = paramInt4;
  }

  Test2(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    this(paramInt1, paramInt2, paramInt3, -1, paramInt4, -1);
  }

  boolean equals(Test2 paramTest2) {
    return (this.m_test == paramTest2.m_test && this.m_tokenIdx == paramTest2.m_tokenIdx && this.m_rightIdx == paramTest2.m_rightIdx && this.m_leftIdx == paramTest2.m_leftIdx && this.m_rightSubIdx == paramTest2.m_rightSubIdx && this.m_leftSubIdx == paramTest2.m_leftSubIdx);
  }

  public String toString() {
    StringBuffer stringBuffer = new StringBuffer(100);
    stringBuffer.append("[Test2: test=");
    stringBuffer.append((this.m_test == 1) ? "NEQ" : "EQ");
    stringBuffer.append(";tokenIdx=");
    stringBuffer.append(this.m_tokenIdx);
    stringBuffer.append(";leftIdx=");
    stringBuffer.append(this.m_leftIdx);
    stringBuffer.append(";leftSubIdx=");
    stringBuffer.append(this.m_leftSubIdx);
    stringBuffer.append(";rightIdx=");
    stringBuffer.append(this.m_rightIdx);
    stringBuffer.append(";rightSubIdx=");
    stringBuffer.append(this.m_rightSubIdx);
    stringBuffer.append("]");
    return stringBuffer.toString();
  }
}
