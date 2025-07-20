package jess;

class _neq implements Userfunction {
  public int name() {
    return RU.putAtom("neq");
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    for (byte b = 2; b < paramValueVector.size(); b++) {
      if (paramValueVector.get(b).equals(paramValueVector.get(1)))
        return Funcall.s_false;
    }
    return Funcall.s_true;
  }
}
