package jess;

class _assert_string extends Fastfunction {
  public int name() {
    return RU.putAtom("assert-string");
  }

  public Value call(ValueVector paramValueVector, Context paramContext, Value paramValue)
      throws ReteException {
    String str = paramValueVector.get(1).stringValue();
    return paramValue.resetValue(paramContext.engine().assertString(str), 16);
  }
}
