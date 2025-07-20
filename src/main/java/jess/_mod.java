package jess;

class _mod extends Fastfunction {
  public int name() {
    return RU.putAtom("mod");
  }

  public Value call(ValueVector paramValueVector, Context paramContext, Value paramValue) throws ReteException {
    int i = (int)paramValueVector.get(1).numericValue();
    int j = (int)paramValueVector.get(2).numericValue();
    return paramValue.resetValue(i % j, 4);
  }
}
