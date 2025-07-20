package jess;

class log10 implements Userfunction {
  private static final double log10 = Math.log(10.0D);

  private int m_name = RU.putAtom("log10");

  public int name() {
    return this.m_name;
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    return new Value(Math.log(paramValueVector.get(1).numericValue()) / log10, 32);
  }
}
