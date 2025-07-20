package jess;

class _undefrule implements Userfunction {
  public int name() {
    return RU.putAtom("undefrule");
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    int i = paramValueVector.get(1).atomValue();
    return paramContext.engine().unDefrule(i);
  }
}
