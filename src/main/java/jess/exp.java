package jess;

class exp implements Userfunction {
  private int m_name = RU.putAtom("exp");

  public int name() {
    return this.m_name;
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    return new Value(Math.pow(Math.E, paramValueVector.get(1).numericValue()), 32);
  }
}
