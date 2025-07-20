package jess;

class strlength implements Userfunction {
  private int m_name = RU.putAtom("str-length");

  public int name() {
    return this.m_name;
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    return new Value(paramValueVector.get(1).stringValue().length(), 4);
  }
}
