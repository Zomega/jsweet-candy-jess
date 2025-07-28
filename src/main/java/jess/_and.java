package jess;

class _and implements Userfunction {
  public int name() {
    return RU.putAtom("and");
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    for (byte b = 1; b < paramValueVector.size(); b++) {
      Value value = paramValueVector.get(b);
      switch (value.type()) {
        case 64:
          value = Funcall.execute(paramContext.expandAction(value.funcallValue()), paramContext);
          break;
      }
      if (value.equals(Funcall.s_false)) return Funcall.s_false;
    }
    return Funcall.s_true;
  }
}
