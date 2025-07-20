package jess;

class stringp implements Userfunction {
  private int m_name = RU.putAtom("stringp");

  public int name() {
    return this.m_name;
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    return (paramValueVector.get(1).type() == 2) ? Funcall.TRUE() : Funcall.FALSE();
  }
}
