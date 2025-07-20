package jess;

class _jess_version_string extends Fastfunction {
  public int name() {
    return RU.putAtom("jess-version-string");
  }

  public Value call(ValueVector paramValueVector, Context paramContext, Value paramValue) throws ReteException {
    return paramValue.resetValue("Jess Version 4.0 3/23/98", 2);
  }
}
