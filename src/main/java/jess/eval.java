package jess;

class eval implements Userfunction {
  private int m_name = RU.putAtom("eval");

  public int name() {
    return this.m_name;
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    String str = paramValueVector.get(1).stringValue();
    return paramContext.engine().executeCommand(str);
  }
}
