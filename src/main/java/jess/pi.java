package jess;

class pi implements Userfunction {
  private int m_name = RU.putAtom("pi");

  private static Value s_pi;

  public int name() {
    return this.m_name;
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    return s_pi;
  }

  static {
    try {
      s_pi = new Value(Math.PI, 32);
    } catch (ReteException reteException) {
    }
  }
}
