package jess;

class _rules implements Userfunction {
  public int name() {
    return RU.putAtom("rules");
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    paramContext.engine().showRules();
    return Funcall.s_true;
  }
}
