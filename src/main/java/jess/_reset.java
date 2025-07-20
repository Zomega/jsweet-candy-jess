package jess;

class _reset implements Userfunction {
  public int name() {
    return RU.putAtom("reset");
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    paramContext.engine().reset();
    return Funcall.s_true;
  }
}
