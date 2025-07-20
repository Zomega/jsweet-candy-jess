package jess;

class symbolp implements Userfunction {
  private int m_name = RU.putAtom("symbolp");

  public int name() {
    return this.m_name;
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    return (paramValueVector.get(1).type() == 1) ? Funcall.TRUE() : Funcall.FALSE();
  }
}
