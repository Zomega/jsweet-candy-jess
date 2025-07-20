package jess;

class _while implements Userfunction {
  public int name() {
    return RU.putAtom("while");
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    Value value = paramValueVector.get(1);
    if (value.type() == 64)
      value = Funcall.execute(paramContext.expandAction(value.funcallValue()), paramContext);
    byte b = 0;
    if (paramValueVector.get(2).equals(Funcall.s_do))
      b++;
    label26: while (!value.equals(Funcall.s_false)) {
      for (int i = 2 + b; i < paramValueVector.size(); i++) {
        Value value1 = paramValueVector.get(i);
        if (value1.type() == 64) {
          Funcall funcall = paramContext.expandAction(value1.funcallValue());
          value = Funcall.execute(funcall, paramContext);
          if (paramContext.returning()) {
            value = paramContext.getReturnValue();
            break label26;
          }
        } else {
          value = value1;
        }
      }
      value = paramValueVector.get(1);
      if (value.type() == 64)
        value = Funcall.execute(paramContext.expandAction(value.funcallValue()), paramContext);
    }
    return value;
  }
}
