package jess;

class setgen implements Userfunction {
  private int m_name = RU.putAtom("setgen");

  public int name() {
    return this.m_name;
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    RU.s_gensymIdx = paramValueVector.get(1).intValue();
    return Funcall.TRUE();
  }
}
