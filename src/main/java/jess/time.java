package jess;

class time implements Userfunction {
  private int m_name = RU.putAtom("time");

  public int name() {
    return this.m_name;
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    return new Value((System.currentTimeMillis() / 1000L), 32);
  }
}
