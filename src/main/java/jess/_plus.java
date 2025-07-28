package jess;

class _plus extends Fastfunction {
  public int name() {
    return RU.putAtom("+");
  }

  public Value call(ValueVector paramValueVector, Context paramContext, Value paramValue)
      throws ReteException {
    double d = 0.0D;
    byte b1 = 4;
    for (byte b2 = 1; b2 < paramValueVector.size(); b2++) {
      Value value = paramValueVector.get(b2);
      d += value.numericValue();
      if (b1 == 4 && value.type() == 32) b1 = 32;
    }
    return paramValue.resetValue(d, b1);
  }
}
