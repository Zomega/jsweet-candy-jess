package jess;

class _gensym_star implements Userfunction {
  public int name() {
    return RU.putAtom("gensym*");
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    return new Value(RU.gensym("gen"), 2);
  }
}
