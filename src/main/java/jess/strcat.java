package jess;

class strcat implements Userfunction {
  private int m_name = RU.putAtom("str-cat");

  public int name() {
    return this.m_name;
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    if (paramValueVector.size() == 2 && paramValueVector.get(1).type() == 2)
      return paramValueVector.get(1);
    StringBuffer stringBuffer = new StringBuffer("");
    for (byte b = 1; b < paramValueVector.size(); b++) {
      Value value = paramValueVector.get(b);
      if (value.type() == 2) {
        stringBuffer.append(value.stringValue());
      } else {
        stringBuffer.append(value.toString());
      }
    }
    return new Value(stringBuffer.toString(), 2);
  }
}
