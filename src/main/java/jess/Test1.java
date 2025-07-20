package jess;

class Test1 {
  static final int EQ = 0;

  static final int NEQ = 1;

  int m_test;

  int m_slotIdx;

  int m_subIdx;

  Value m_slotValue;

  Test1(int paramInt1, int paramInt2, Value paramValue) {
    this(paramInt1, paramInt2, -1, paramValue);
  }

  Test1(int paramInt1, int paramInt2, int paramInt3, Value paramValue) {
    this.m_test = paramInt1;
    this.m_slotIdx = paramInt2;
    this.m_subIdx = paramInt3;
    this.m_slotValue = paramValue;
  }

  public String toString() {
    StringBuffer stringBuffer = new StringBuffer(100);
    stringBuffer.append("[Test1: test=");
    stringBuffer.append((this.m_test == 1) ? "NEQ" : "EQ");
    stringBuffer.append(";slot_idx=");
    stringBuffer.append(this.m_slotIdx);
    stringBuffer.append(";sub_idx=");
    stringBuffer.append(this.m_subIdx);
    stringBuffer.append(";slot_value=");
    stringBuffer.append(this.m_slotValue);
    stringBuffer.append("]");
    return stringBuffer.toString();
  }
}
