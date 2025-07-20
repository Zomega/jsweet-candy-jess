package jess;

public class Deftemplate {
  private ValueVector m_deft = new ValueVector();

  int m_name;

  int m_ordered;

  String m_docstring;

  public final int name() {
    return this.m_name;
  }

  public final String docstring() {
    return this.m_docstring;
  }

  public final void docstring(String paramString) {
    this.m_docstring = paramString;
  }

  public Deftemplate(String paramString, int paramInt) throws ReteException {
    this.m_name = RU.putAtom(paramString);
    this.m_ordered = paramInt;
    this.m_deft.setLength(3);
    this.m_deft.set(new Value(this.m_name, 1), 0);
    this.m_deft.set(new Value(this.m_ordered, 1024), 1);
    this.m_deft.set(new Value(0, 16), 2);
  }

  public Deftemplate(ValueVector paramValueVector) throws ReteException {
    this.m_name = paramValueVector.get(0).atomValue();
    this.m_ordered = paramValueVector.get(1).descriptorValue();
  }

  public void addSlot(String paramString, Value paramValue) throws ReteException {
    if (this.m_ordered != 256)
      throw new ReteException("AddSlot", "Ordered deftemplates cannot have slots:", paramString);
    this.m_deft.add(new Value(paramString, 16384));
    this.m_deft.add(paramValue);
  }

  public void addMultiSlot(String paramString, Value paramValue) throws ReteException {
    if (this.m_ordered != 256)
      throw new ReteException("AddSlot", "Ordered deftemplates cannot have slots:", paramString);
    this.m_deft.add(new Value(paramString, 32768));
    this.m_deft.add(paramValue);
  }

  ValueVector deftemplateData() {
    return this.m_deft;
  }

  static int slotType(ValueVector paramValueVector, String paramString) throws ReteException {
    if (paramValueVector.get(1).descriptorValue() != 256)
      return 0;
    int i = RU.putAtom(paramString);
    for (byte b = 3; b < paramValueVector.size(); b += 2) {
      if (paramValueVector.get(b + 0).atomValue() == i)
        return paramValueVector.get(b + 0).type();
    }
    return 0;
  }

  public String toString() {
    try {
      StringBuffer stringBuffer = new StringBuffer(100);
      stringBuffer.append("[Deftemplate: ");
      stringBuffer.append(RU.getAtom(this.m_name));
      stringBuffer.append(" ");
      stringBuffer.append((this.m_ordered == 128) ? "(ordered)" : "(unordered)");
      if (this.m_docstring != null)
        stringBuffer.append(" \"" + this.m_docstring + "\" ");
      if (this.m_ordered == 256) {
        stringBuffer.append("slots:");
        for (byte b = 3; b < this.m_deft.size(); b += 2) {
          stringBuffer.append(" ");
          stringBuffer.append(this.m_deft.get(b + 0).stringValue());
          stringBuffer.append(" default: ");
          stringBuffer.append(this.m_deft.get(b + 1).toString());
          stringBuffer.append(";");
        }
      }
      stringBuffer.append("]");
      return stringBuffer.toString();
    } catch (ReteException reteException) {
      return reteException.toString();
    }
  }
}
