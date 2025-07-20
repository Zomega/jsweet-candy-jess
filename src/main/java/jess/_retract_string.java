package jess;

class _retract_string implements Userfunction {
  public int name() {
    return RU.putAtom("retract-string");
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    for (byte b = 1; b < paramValueVector.size(); b++)
      paramContext.engine().retractString(paramValueVector.get(b).stringValue());
    return Funcall.s_true;
  }
}
