package jess;

class upcase implements Userfunction {
  private int m_name = RU.putAtom("upcase");

  public int name() {
    return this.m_name;
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    return new Value(paramValueVector.get(1).stringValue().toUpperCase(), 2);
  }
}
