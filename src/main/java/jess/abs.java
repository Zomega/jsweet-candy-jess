package jess;

class abs implements Userfunction {
  private int m_name = RU.putAtom("abs");

  public int name() {
    return this.m_name;
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    Value value = paramValueVector.get(1);
    return new Value(Math.abs(value.numericValue()), value.type());
  }
}
