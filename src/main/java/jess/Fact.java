package jess;

public class Fact {
  private ValueVector m_fact;

  private ValueVector m_deft;

  private int m_name;

  private int m_ordered;

  public Fact(String paramString, int paramInt, Rete paramRete) throws ReteException {
    this.m_name = RU.putAtom(paramString);
    this.m_ordered = paramInt;
    this.m_deft = findDeftemplate(paramString, this.m_ordered, paramRete);
    this.m_fact = createNewFact(this.m_deft);
  }

  public Fact(ValueVector paramValueVector, Rete paramRete) throws ReteException {
    this.m_name = paramValueVector.get(0).atomValue();
    this.m_ordered = paramValueVector.get(1).descriptorValue();
    this.m_deft = findDeftemplate(RU.getAtom(this.m_name), this.m_ordered, paramRete);
    this.m_fact = (ValueVector)paramValueVector.clone();
  }

  private ValueVector createNewFact(ValueVector paramValueVector) {
    ValueVector valueVector = new ValueVector();
    int i = (this.m_deft.size() - 3) / 2 + 3;
    valueVector.setLength(i);
    valueVector.set(paramValueVector.get(0), 0);
    valueVector.set(paramValueVector.get(1), 1);
    valueVector.set(paramValueVector.get(2), 2);
    byte b1 = 4;
    for (byte b2 = 3; b2 < i; b2++) {
      valueVector.set(paramValueVector.get(b1), b2);
      b1 += 2;
    }
    return valueVector;
  }

  private ValueVector findDeftemplate(String paramString, int paramInt, Rete paramRete) throws ReteException {
    ValueVector valueVector = paramRete.findDeftemplate(paramString);
    if (valueVector != null) {
      if (valueVector.get(1).descriptorValue() != paramInt)
        throw new ReteException("Fact::findDeftemplate", "Attempt to duplicate implied deftemplate:", paramString);
    } else {
      if (paramInt == 256)
        throw new ReteException("Fact::findDeftemplate", "Can't create implied unordered deftempl:", paramString);
      valueVector = paramRete.addDeftemplate(new Deftemplate(paramString, paramInt));
    }
    return valueVector;
  }

  final int findSlot(String paramString) throws ReteException {
    return findSlot(RU.putAtom(paramString));
  }

  final int findSlot(int paramInt) throws ReteException {
    if (this.m_ordered == 128)
      throw new ReteException("Fact::FindSlot", "Attempt to find named slot in ordered fact", "");
    byte b;
    for (b = 3; b < this.m_deft.size() && this.m_deft.get(b + 0).atomValue() != paramInt; b += 2);
    if (b >= this.m_deft.size())
      throw new ReteException("Fact::AddValue", "Attempt to add field with invalid slotname", RU.getAtom(paramInt));
    return (b - 3) / 2 + 3;
  }

  public final Value findValue(String paramString) throws ReteException {
    int i = findSlot(paramString);
    return this.m_fact.get(i);
  }

  public final void addValue(String paramString1, String paramString2, int paramInt) throws ReteException {
    int i = findSlot(paramString1);
    this.m_fact.set(new Value(paramString2, paramInt), i);
  }

  public final void addValue(String paramString, int paramInt1, int paramInt2) throws ReteException {
    int i = findSlot(paramString);
    this.m_fact.set(new Value(paramInt1, paramInt2), i);
  }

  public final void addValue(String paramString, double paramDouble, int paramInt) throws ReteException {
    int i = findSlot(paramString);
    this.m_fact.set(new Value(paramDouble, paramInt), i);
  }

  public final void addValue(String paramString, Funcall paramFuncall, int paramInt) throws ReteException {
    int i = findSlot(paramString);
    this.m_fact.set(new Value(paramFuncall, paramInt), i);
  }

  public final void addValue(String paramString, Value paramValue) throws ReteException {
    int i = findSlot(paramString);
    this.m_fact.set(new Value(paramValue), i);
  }

  public final void addValue(String paramString, int paramInt) throws ReteException {
    addValue(RU.putAtom(paramString), paramInt);
  }

  public final void addValue(int paramInt1, int paramInt2) throws ReteException {
    if (this.m_ordered == 256)
      throw new ReteException("Fact::AddValue", "Can't add ordered field to unordered fact", "");
    this.m_fact.add(new Value(paramInt1, paramInt2));
  }

  public final void addValue(double paramDouble, int paramInt) throws ReteException {
    if (this.m_ordered == 256)
      throw new ReteException("Fact::AddValue", "Can't add ordered field to unordered fact", "");
    this.m_fact.add(new Value(paramDouble, paramInt));
  }

  public final void addValue(Funcall paramFuncall, int paramInt) throws ReteException {
    if (this.m_ordered == 256)
      throw new ReteException("Fact::AddValue", "Can't add ordered field to unordered fact", "");
    this.m_fact.add(new Value(paramFuncall, paramInt));
  }

  public final void addValue(Value paramValue) throws ReteException {
    this.m_fact.add(new Value(paramValue));
  }

  public final ValueVector factData() {
    return this.m_fact;
  }

  public final ValueVector deft() {
    return this.m_deft;
  }

  public String toString() {
    StringBuffer stringBuffer = new StringBuffer(100);
    try {
      stringBuffer.append("(");
      stringBuffer.append(RU.getAtom(this.m_name));
      if (this.m_ordered == 128) {
        for (byte b = 3; b < this.m_fact.size(); b++) {
          stringBuffer.append(" ");
          stringBuffer.append(this.m_fact.get(b));
        }
      } else {
        int i = (this.m_deft.size() - 3) / 2;
        for (byte b = 0; b < i; b++) {
          stringBuffer.append(" (");
          stringBuffer.append(this.m_deft.get(3 + b * 2 + 0).stringValue());
          stringBuffer.append(" ");
          stringBuffer.append(this.m_fact.get(3 + b));
          stringBuffer.append(")");
        }
      }
      stringBuffer.append(")");
      return stringBuffer.toString();
    } catch (ReteException reteException) {
      return reteException.toString();
    }
  }
}
