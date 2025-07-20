package jess;

class _jess_version_number extends Fastfunction {
  public int name() {
    return RU.putAtom("jess-version-number");
  }

  public Value call(ValueVector paramValueVector, Context paramContext, Value paramValue) throws ReteException {
    return paramValue.resetValue(4.0D, 32);
  }
}
