package jess;

class _equals implements Userfunction {
  public int name() {
    return RU.putAtom("=");
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    for (byte b = 2; b < paramValueVector.size(); b++) {
      if (paramValueVector.get(b).numericValue() != paramValueVector.get(1).numericValue())
        return Funcall.s_false;
    }
    return Funcall.s_true;
  }
}
