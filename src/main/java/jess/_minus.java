package jess;

class _minus extends Fastfunction {
  public int name() {
    return RU.putAtom("-");
  }

  public Value call(ValueVector paramValueVector, Context paramContext, Value paramValue)
      throws ReteException {
    Value value = paramValueVector.get(1);
    int i = value.type();
    double d = value.numericValue();
    for (byte b = 2; b < paramValueVector.size(); b++) {
      value = paramValueVector.get(b);
      d -= value.numericValue();
      if (i == 4 && value.type() == 32) i = 32;
    }
    return paramValue.resetValue(d, i);
  }
}
