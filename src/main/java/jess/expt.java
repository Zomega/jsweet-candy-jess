package jess;

class expt implements Userfunction {
  private int m_name = RU.putAtom("**");

  public int name() {
    return this.m_name;
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    return new Value(
        Math.pow(paramValueVector.get(1).numericValue(), paramValueVector.get(2).numericValue()),
        32);
  }
}
