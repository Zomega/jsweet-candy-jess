package jess;

class _not implements Userfunction {
  public int name() {
    return RU.putAtom("not");
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    return paramValueVector.get(1).equals(Funcall.s_false) ? Funcall.s_true : Funcall.s_false;
  }
}
