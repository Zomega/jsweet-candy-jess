package jess;

class _retract implements Userfunction {
  public int name() {
    return RU.putAtom("retract");
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    Value value = paramValueVector.get(1);
    if (value.type() == 1 && value.stringValue().equals("*")) {
      paramContext.engine().removeFacts();
    } else {
      Rete rete = paramContext.engine();
      Userfunction userfunction = rete.findUserfunction("undefinstance");
      for (byte b = 1; b < paramValueVector.size(); b++) {
        ValueVector valueVector = rete.findFactByID(paramValueVector.get(b).factIDValue());
        if (valueVector != null) {
          if (userfunction != null)
            try {
              Fact fact = new Fact(valueVector, rete);
              Value value1 = fact.findValue("OBJECT");
              Funcall funcall = new Funcall("undefinstance", rete);
              funcall.add(value1);
              Funcall.simpleExecute(funcall, rete.globalContext());
            } catch (Exception exception) {
            }
          rete._retract(valueVector);
        }
      }
    }
    return Funcall.s_true;
  }
}
