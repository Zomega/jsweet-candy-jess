package jess;

class round implements Userfunction {
  private int m_name = RU.putAtom("round");

  public int name() {
    return this.m_name;
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    return new Value(Math.round(paramValueVector.get(1).numericValue()), 4);
  }
}
