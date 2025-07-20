package jess;

class numberp implements Userfunction {
  private int m_name = RU.putAtom("numberp");

  public int name() {
    return this.m_name;
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    return (paramValueVector.get(1).type() == 4 || paramValueVector.get(1).type() == 32) ? Funcall.TRUE() : Funcall.FALSE();
  }
}
