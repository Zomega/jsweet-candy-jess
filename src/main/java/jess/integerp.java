package jess;

class integerp implements Userfunction {
  private int m_name = RU.putAtom("integerp");

  public int name() {
    return this.m_name;
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    return (paramValueVector.get(1).type() == 4) ? Funcall.TRUE() : Funcall.FALSE();
  }
}
