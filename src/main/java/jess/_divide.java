package jess;

class _divide extends Fastfunction {
  public int name() {
    return RU.putAtom("/");
  }

  public Value call(ValueVector paramValueVector, Context paramContext, Value paramValue) throws ReteException {
    double d = paramValueVector.get(1).numericValue();
    for (byte b = 2; b < paramValueVector.size(); b++)
      d /= paramValueVector.get(b).floatValue();
    return paramValue.resetValue(d, 32);
  }
}
