package jess;

class _foreach implements Userfunction {
  public int name() {
    return RU.putAtom("foreach");
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    int i = paramValueVector.get(1).variableValue();
    ValueVector valueVector = paramValueVector.get(2).listValue();
    Value value = Funcall.NIL();
    for (byte b = 0; b < valueVector.size(); b++) {
      paramContext.setVariable(i, valueVector.get(b));
      for (byte b1 = 3; b1 < paramValueVector.size(); b1++) {
        value = paramValueVector.get(b1);
        switch (value.type()) {
          case 64:
            value = Funcall.execute(paramContext.expandAction(value.funcallValue()), paramContext);
            if (paramContext.returning()) {
              value = paramContext.getReturnValue();
              paramContext.clearReturnValue();
              return value;
            }
            break;
          case 8:
          case 8192:
            value = (paramContext.findBinding(value.variableValue())).m_val;
            break;
        }
      }
    }
    return value;
  }
}
