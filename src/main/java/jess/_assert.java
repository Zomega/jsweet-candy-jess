package jess;

class _assert extends Fastfunction {
  public int name() {
    return RU.putAtom("assert");
  }

  public Value call(ValueVector paramValueVector, Context paramContext, Value paramValue) throws ReteException {
    int i = -1;
    for (byte b = 1; b < paramValueVector.size(); b++) {
      ValueVector valueVector = paramValueVector.get(b).factValue();
      i = paramContext.engine().assert(valueVector);
    }
    return (i != -1) ? paramValue.resetValue(i, 16) : Funcall.s_false;
  }
}
