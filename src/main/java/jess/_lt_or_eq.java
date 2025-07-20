package jess;

class _lt_or_eq implements Userfunction {
  public int name() {
    return RU.putAtom("<=");
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    for (byte b = 1; b < paramValueVector.size() - 1; b++) {
      double d1 = paramValueVector.get(b).numericValue();
      double d2 = paramValueVector.get(b + 1).numericValue();
      if (d1 > d2)
        return Funcall.s_false;
    }
    return Funcall.s_true;
  }
}
