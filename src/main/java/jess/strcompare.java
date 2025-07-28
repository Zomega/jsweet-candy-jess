package jess;

class strcompare implements Userfunction {
  private int m_name = RU.putAtom("str-compare");

  public int name() {
    return this.m_name;
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    return new Value(
        paramValueVector.get(1).stringValue().compareTo(paramValueVector.get(2).stringValue()), 4);
  }
}
