package jess;

class Binding implements Cloneable {
  int m_name;

  int m_factIndex;

  int m_slotIndex;

  int m_subIndex;

  Value m_val;

  public Binding(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    this.m_name = paramInt1;
    this.m_factIndex = paramInt2;
    this.m_slotIndex = paramInt3;
    this.m_subIndex = paramInt4;
    this.m_val = null;
  }

  public Binding(int paramInt, Value paramValue) {
    this.m_name = paramInt;
    this.m_factIndex = -2;
    this.m_slotIndex = -2;
    this.m_subIndex = -1;
    this.m_val = paramValue;
  }

  public Object clone() {
    Binding binding = new Binding(this.m_name, this.m_factIndex, this.m_slotIndex, this.m_subIndex);
    binding.m_val = this.m_val;
    return binding;
  }

  public String toString() {
    StringBuffer stringBuffer = new StringBuffer(100);
    stringBuffer.append("[Binding: ");
    stringBuffer.append(RU.getAtom(this.m_name));
    stringBuffer.append(";factIndex=" + this.m_factIndex);
    stringBuffer.append(";slotIndex=" + this.m_slotIndex);
    stringBuffer.append(";subIndex=" + this.m_subIndex);
    stringBuffer.append(";val=" + this.m_val);
    stringBuffer.append("]");
    return stringBuffer.toString();
  }
}
