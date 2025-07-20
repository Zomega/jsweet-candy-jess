package jess;

class _halt implements Userfunction {
  public int name() {
    return RU.putAtom("halt");
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    paramContext.engine().halt();
    return Funcall.s_true;
  }
}
