package jess;

class _if implements Userfunction {
  public int name() {
    return RU.putAtom("if");
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    Value value = paramValueVector.get(1);
    if (value.type() == 64)
      value = Funcall.execute(value.funcallValue(), paramContext);
    if (!value.equals(Funcall.s_false)) {
      value = Funcall.s_false;
      for (byte b1 = 3; b1 < paramValueVector.size(); b1++) {
        Value value1 = paramValueVector.get(b1);
        if (value1.type() == 64) {
          Funcall funcall = paramContext.expandAction(paramValueVector.get(b1).funcallValue());
          value = Funcall.execute(funcall, paramContext);
          if (paramContext.returning()) {
            value = paramContext.getReturnValue();
            break;
          }
        } else {
          if (value1.equals(Funcall.s_else))
            break;
          value = value1;
          break;
        }
      }
      return value;
    }
    value = Funcall.s_false;
    boolean bool = false;
    for (byte b = 3; b < paramValueVector.size(); b++) {
      Value value1 = paramValueVector.get(b);
      if (bool) {
        if (value1.type() == 64) {
          Funcall funcall = paramContext.expandAction(paramValueVector.get(b).funcallValue());
          value = Funcall.execute(funcall, paramContext);
          if (paramContext.returning()) {
            value = paramContext.getReturnValue();
            break;
          }
        } else {
          value = value1;
        }
      } else if (value1.equals(Funcall.s_else)) {
        bool = true;
      }
    }
    return value;
  }
}
