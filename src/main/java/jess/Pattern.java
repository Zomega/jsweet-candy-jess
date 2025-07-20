package jess;

class Pattern {
  private ValueVector m_deft;

  int m_nvalues;

  public Test1[][] m_tests;

  public int[] m_slotLengths;

  private int m_negated;

  private int m_class;

  private int m_ordered;

  boolean m_hasVariables;

  boolean m_compacted;

  public int size() {
    return this.m_nvalues + 3;
  }

  public Pattern(String paramString, int paramInt1, Rete paramRete, int paramInt2) throws ReteException {
    this.m_class = RU.putAtom(paramString);
    this.m_ordered = paramInt1;
    this.m_nvalues = 0;
    this.m_deft = findDeftemplate(paramString, paramRete);
    this.m_negated = paramInt2;
    if (this.m_ordered == 128) {
      this.m_tests = new Test1[32][];
    } else {
      this.m_nvalues = (this.m_deft.size() - 3) / 2;
      this.m_tests = new Test1[this.m_nvalues][];
      this.m_slotLengths = new int[this.m_nvalues];
      for (byte b = 0; b < this.m_nvalues; b++)
        this.m_slotLengths[b] = -1;
    }
  }

  ValueVector findDeftemplate(String paramString, Rete paramRete) throws ReteException {
    this.m_deft = paramRete.findDeftemplate(paramString);
    if (this.m_deft != null) {
      if (this.m_ordered != this.m_deft.get(1).descriptorValue())
        throw new ReteException("Pattern::FindDeftemplate", "Attempt to duplicate implied deftemplate:", paramString);
    } else {
      if (this.m_ordered == 256)
        throw new ReteException("Pattern::FindDeftemplate", "Attempt to create implied unordered deftemplate:", paramString);
      this.m_deft = paramRete.addDeftemplate(new Deftemplate(paramString, this.m_ordered));
    }
    return this.m_deft;
  }

  public void setMultislotLength(String paramString, int paramInt) throws ReteException {
    if (this.m_ordered == 128)
      throw new ReteException("Pattern::SetMultislotLength", "Attempt to set slot length on ordered pattern", "");
    byte b;
    for (b = 3; b < this.m_deft.size() && !this.m_deft.get(b).stringValue().equals(paramString); b += 2);
    if (b >= this.m_deft.size())
      throw new ReteException("Pattern::SetMultislotLength", "Attempt to set length of invalid slotname", paramString);
    int i = b - 3;
    i /= 2;
    this.m_slotLengths[i] = paramInt;
  }

  public void addTest(String paramString, Value paramValue, int paramInt, boolean paramBoolean) throws ReteException {
    addTest(RU.putAtom(paramString), paramValue, paramInt, paramBoolean);
  }

  public void addTest(int paramInt1, Value paramValue, int paramInt2, boolean paramBoolean) throws ReteException {
    if (this.m_ordered == 128)
      throw new ReteException("Pattern::AddValue", "Attempt to add slot integer to ordered pattern", "");
    byte b1;
    for (b1 = 3; b1 < this.m_deft.size() && this.m_deft.get(b1).atomValue() != paramInt1; b1 += 2);
    if (b1 >= this.m_deft.size())
      throw new ReteException("Pattern::AddValue", "Attempt to add field with invalid slotname", RU.getAtom(paramInt1));
    int i = b1 - 3;
    i /= 2;
    if (this.m_tests[i] == null)
      this.m_tests[i] = new Test1[32];
    byte b2;
    for (b2 = 0; this.m_tests[i][b2] != null; b2++);
    if (paramBoolean) {
      this.m_tests[i][b2] = new Test1(1, b1, paramInt2, paramValue);
    } else {
      this.m_tests[i][b2] = new Test1(0, b1, paramInt2, paramValue);
    }
  }

  public void addTest(Value paramValue, boolean paramBoolean) throws ReteException {
    if (this.m_ordered == 256)
      throw new ReteException("Pattern::AddValue", "Attempt to add ordered field to unordered pattern", "");
    int i = 3 + this.m_nvalues * 2;
    if (this.m_nvalues < 32) {
      if (this.m_tests[this.m_nvalues] == null)
        this.m_tests[this.m_nvalues] = new Test1[32];
      byte b;
      for (b = 0; this.m_tests[this.m_nvalues][b] != null; b++);
      if (paramBoolean) {
        this.m_tests[this.m_nvalues][b] = new Test1(1, i, paramValue);
      } else {
        this.m_tests[this.m_nvalues][b] = new Test1(0, i, paramValue);
      }
    } else {
      throw new ReteException("Pattern::AddValue", "MaxFields exceeded", "");
    }
  }

  public void advance() {
    this.m_nvalues++;
  }

  public int negated() {
    return this.m_negated;
  }

  public int name() {
    return this.m_class;
  }

  public int ordered() {
    return this.m_ordered;
  }

  public void compact() {
    if (this.m_compacted)
      return;
    this.m_compacted = true;
    if (this.m_ordered == 128) {
      Test1[][] arrayOfTest1 = new Test1[this.m_nvalues][];
      for (byte b1 = 0; b1 < this.m_nvalues; b1++)
        arrayOfTest1[b1] = this.m_tests[b1];
      this.m_tests = arrayOfTest1;
    }
    int i = this.m_tests.length;
    for (byte b = 0; b < i; b++) {
      if (this.m_tests[b] != null) {
        byte b1;
        for (b1 = 0; this.m_tests[b][b1] != null; b1++);
        if (b1 != 0) {
          Test1[] arrayOfTest1 = new Test1[b1];
          for (byte b2 = 0; b2 < b1; b2++)
            arrayOfTest1[b2] = this.m_tests[b][b2];
          this.m_tests[b] = arrayOfTest1;
        } else {
          this.m_tests[b] = null;
        }
      }
    }
  }

  public String toString() {
    null = "[Pattern: " + RU.getAtom(this.m_class) + " ";
    null = null + ((this.m_ordered == 128) ? "(ordered)" : "(unordered)");
    if (this.m_negated != 0)
      null = null + " (negated : " + this.m_negated + ")";
    return null + "]";
  }

  String ppPattern(Rete paramRete) throws ReteException {
    return doPPPattern(paramRete);
  }

  String doPPPattern(Rete paramRete) throws ReteException {
    StringBuffer stringBuffer = new StringBuffer(100);
    stringBuffer.append("(");
    stringBuffer.append(RU.getAtom(this.m_class));
    stringBuffer.append(" ");
    boolean bool = (this.m_class == RU.putAtom("test")) ? true : false;
    if (this.m_ordered == 256) {
      for (byte b = 0; b < this.m_tests.length; b++) {
        if (this.m_tests[b] != null) {
          for (byte b1 = 0; b1 < (this.m_tests[b]).length; b1++) {
            if (this.m_tests[b][b1] != null) {
              Test1 test1 = this.m_tests[b][b1];
              String str = RU.getAtom(this.m_deft.get(test1.m_slotIdx).atomValue());
              if (b1 == 0) {
                stringBuffer.append("(");
                stringBuffer.append(str);
                stringBuffer.append(" ");
              }
              if (b1 != 0)
                stringBuffer.append("&");
              stringBuffer.append(ppTest1(test1, paramRete, bool));
            }
          }
          stringBuffer.append(") ");
        }
      }
    } else {
      for (byte b = 0; b < this.m_tests.length; b++) {
        if (this.m_tests[b] != null) {
          for (byte b1 = 0; b1 < (this.m_tests[b]).length; b1++) {
            if (this.m_tests[b][b1] != null) {
              Test1 test1 = this.m_tests[b][b1];
              if (b1 != 0)
                stringBuffer.append("&");
              stringBuffer.append(ppTest1(test1, paramRete, bool));
            }
          }
          stringBuffer.append(" ");
        }
      }
    }
    stringBuffer.append(")");
    return stringBuffer.toString();
  }

  private String ppTest1(Test1 paramTest1, Rete paramRete, boolean paramBoolean) throws ReteException {
    Funcall funcall;
    String str = "";
    if (paramTest1.m_test == 1)
      str = str + "~";
    int i = paramTest1.m_slotValue.type();
    switch (i) {
      case 1:
      case 2:
      case 4:
      case 8:
      case 32:
        str = str + paramTest1.m_slotValue.toString();
        break;
      case 64:
        funcall = paramTest1.m_slotValue.funcallValue();
        if (!paramBoolean)
          str = str + ":";
        str = str + funcall.ppFuncall(paramRete);
        break;
    }
    return str;
  }
}
