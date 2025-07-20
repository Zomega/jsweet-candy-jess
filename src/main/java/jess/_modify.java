package jess;

class _modify implements Userfunction {
  public int name() {
    return RU.putAtom("modify");
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    ValueVector valueVector;
    if ((valueVector = paramContext.m_engine.findFactByID(paramValueVector.get(1).factIDValue())) == null)
      throw new ReteException("_modify::call", "modify: no such fact", "");
    Fact fact = new Fact(valueVector, paramContext.engine());
    Rete rete = paramContext.engine();
    if (rete.findUserfunction("definstance") != null)
      try {
        Value value = fact.findValue("OBJECT");
        Funcall funcall = new Funcall("set", rete);
        funcall.add(value);
        funcall.add(new Value("set", 2));
        funcall.setLength(4);
        for (byte b1 = 2; b1 < paramValueVector.size(); b1++) {
          ValueVector valueVector1 = paramValueVector.get(b1).listValue();
          funcall.set(valueVector1.get(0), 2);
          if (valueVector1.size() > 2) {
            ValueVector valueVector2 = new ValueVector();
            for (byte b2 = 1; b2 < valueVector1.size(); b2++)
              valueVector2.add(valueVector1.get(b2));
            funcall.set(new Value(valueVector2, 512), 3);
          } else {
            funcall.set(valueVector1.get(1), 3);
          }
          Funcall.simpleExecute(funcall, rete.globalContext());
        }
        return Funcall.TRUE();
      } catch (ReteException reteException) {}
    for (byte b = 2; b < paramValueVector.size(); b++) {
      ValueVector valueVector1 = paramValueVector.get(b).listValue();
      int i = (fact.findSlot(valueVector1.get(0).atomValue()) - 3) * 2 + 3;
      if (fact.deft().get(i).type() == 32768 && (valueVector1.size() < 2 || valueVector1.get(1).type() != 512)) {
        ValueVector valueVector2 = new ValueVector();
        for (byte b1 = 1; b1 < valueVector1.size(); b1++)
          valueVector2.add(valueVector1.get(b1));
        fact.addValue(valueVector1.get(0).stringValue(), new Value(valueVector2, 512));
      } else {
        fact.addValue(valueVector1.get(0).stringValue(), valueVector1.get(1));
      }
    }
    paramContext.engine().retract(valueVector.get(2).factIDValue());
    valueVector = fact.factData();
    return new Value(paramContext.engine().assert(valueVector), 16);
  }
}
