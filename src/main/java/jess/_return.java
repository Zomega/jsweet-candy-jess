package jess;

class _return implements Userfunction {
  public int name() {
    return RU.putAtom("return");
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    return paramContext.setReturnValue(paramValueVector.get(1));
  }
}
