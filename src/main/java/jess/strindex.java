package jess;

class strindex implements Userfunction {
  private int m_name = RU.putAtom("str-index");

  public int name() {
    return this.m_name;
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    int i = paramValueVector.get(2).stringValue().indexOf(paramValueVector.get(1).stringValue());
    return (i == -1) ? Funcall.FALSE() : new Value(i + 1, 4);
  }
}
