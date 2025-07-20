package jess;

class _bind implements Userfunction {
  public int name() {
    return RU.putAtom("bind");
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    paramContext.setVariable(paramValueVector.get(1).variableValue(), paramValueVector.get(2));
    return paramValueVector.get(2);
  }
}
