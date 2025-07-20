package jess;

class sqrt implements Userfunction {
  private int m_name = RU.putAtom("sqrt");

  public int name() {
    return this.m_name;
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    return new Value(Math.sqrt(paramValueVector.get(1).numericValue()), 32);
  }
}
