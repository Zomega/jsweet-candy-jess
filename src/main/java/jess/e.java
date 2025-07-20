package jess;

class e implements Userfunction {
  private int m_name = RU.putAtom("e");

  private static Value s_e;

  public int name() {
    return this.m_name;
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    return s_e;
  }

  static {
    try {
      s_e = new Value(Math.E, 32);
    } catch (ReteException reteException) {}
  }
}
