package jess;

class lexemep implements Userfunction {
  private int m_name = RU.putAtom("lexemep");

  public int name() {
    return this.m_name;
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    return (paramValueVector.get(1).type() == 1 || paramValueVector.get(1).type() == 2)
        ? Funcall.TRUE()
        : Funcall.FALSE();
  }
}
