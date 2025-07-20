package jess;

class substring implements Userfunction {
  private int m_name = RU.putAtom("sub-string");

  public int name() {
    return this.m_name;
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    int i = (int)paramValueVector.get(1).numericValue() - 1;
    int j = (int)paramValueVector.get(2).numericValue();
    String str = paramValueVector.get(3).stringValue();
    if (i < 0 || i > str.length() - 1 || j > str.length() || j <= 0)
      throw new ReteException("sub-string", "Indices must be between 1 and " + str.length(), "");
    return new Value(str.substring(i, j), 2);
  }
}
