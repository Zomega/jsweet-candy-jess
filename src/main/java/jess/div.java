package jess;

class div implements Userfunction {
  private int m_name = RU.putAtom("div");

  public int name() {
    return this.m_name;
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    int i = (int) paramValueVector.get(1).numericValue();
    int j = (int) paramValueVector.get(2).numericValue();
    return new Value(i / j, 4);
  }
}
