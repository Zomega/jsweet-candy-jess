package jess;

class multifieldp implements Userfunction {
  private int m_name = RU.putAtom("multifieldp");

  public int name() {
    return this.m_name;
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    return (paramValueVector.get(1).type() == 512) ? Funcall.TRUE() : Funcall.FALSE();
  }
}
