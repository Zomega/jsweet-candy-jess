package jess;

class evenp implements Userfunction {
  private int m_name = RU.putAtom("evenp");

  public int name() {
    return this.m_name;
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    boolean bool = ((int) paramValueVector.get(1).numericValue() % 2 == 0) ? true : false;
    return bool ? Funcall.TRUE() : Funcall.FALSE();
  }
}
