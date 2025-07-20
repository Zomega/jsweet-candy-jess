package jess;

class _float implements Userfunction {
  private int m_name = RU.putAtom("float");

  public int name() {
    return this.m_name;
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    return new Value(paramValueVector.get(1).numericValue(), 32);
  }
}
