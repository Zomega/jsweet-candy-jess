package jess;

class _clear implements Userfunction {
  public int name() {
    return RU.putAtom("clear");
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    paramContext.engine().clear();
    return Funcall.s_true;
  }
}
