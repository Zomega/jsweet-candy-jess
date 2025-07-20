package jess;

class _facts implements Userfunction {
  public int name() {
    return RU.putAtom("facts");
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    paramContext.engine().showFacts();
    return Funcall.s_true;
  }
}
