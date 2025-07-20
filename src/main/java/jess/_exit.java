package jess;

class _exit implements Userfunction {
  public int name() {
    return RU.putAtom("exit");
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    System.exit(0);
    return Funcall.s_true;
  }
}
