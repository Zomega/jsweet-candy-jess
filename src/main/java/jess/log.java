package jess;

class log implements Userfunction {
  private int m_name = RU.putAtom("log");

  public int name() {
    return this.m_name;
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    return new Value(Math.log(paramValueVector.get(1).numericValue()), 32);
  }
}
