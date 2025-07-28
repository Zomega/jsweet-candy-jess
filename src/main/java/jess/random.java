package jess;

class random implements Userfunction {
  private int m_name = RU.putAtom("random");

  public int name() {
    return this.m_name;
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    return new Value((int) (Math.random() * 65536.0D), 4);
  }
}
