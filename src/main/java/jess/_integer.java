package jess;

class _integer implements Userfunction {
  private int m_name = RU.putAtom("integer");

  public int name() {
    return this.m_name;
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    return new Value((int) paramValueVector.get(1).numericValue(), 4);
  }
}
