package jess;

class ppdefrule implements Userfunction {
  private int m_name = RU.putAtom("ppdefrule");

  public int name() {
    return this.m_name;
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    String str = paramValueVector.get(1).stringValue();
    Defrule defrule = paramContext.engine().findDefrule(str);
    return new Value(defrule.ppRule(), 2);
  }
}
