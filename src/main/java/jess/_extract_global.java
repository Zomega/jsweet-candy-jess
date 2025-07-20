package jess;

class _extract_global implements Userfunction {
  public int name() {
    return RU.putAtom("get-var");
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    return (paramContext.findBinding(paramValueVector.get(1).atomValue())).m_val;
  }
}
