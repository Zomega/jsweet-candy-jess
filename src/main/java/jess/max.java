package jess;

class max implements Userfunction {
  private int m_name = RU.putAtom("max");

  public int name() {
    return this.m_name;
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    Value value1 = paramValueVector.get(1);
    Value value2 = paramValueVector.get(2);
    byte b = (value1.type() == 32 || value2.type() == 32) ? 32 : 4;
    return new Value(Math.max(value1.numericValue(), value2.numericValue()), b);
  }
}
